package com.example

import com.example.dto.Error
import com.example.exception.BusinessRuleException
import com.example.exception.ResourceNotFoundException
import com.example.repository.SearchCriteriaRepository
import com.example.routes.routes
import com.example.serializer.DateTimeAdapter
import com.example.serializer.UUIDAdapter
import com.example.service.TwitterService
import com.example.twitter.TwitterClient
import com.ryanharter.ktor.moshi.moshi
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

val databaseUser = "pepe"
val databasePassword = "12345678"
val databaseUrl = "jdbc:postgresql://localhost/tweet-query-db"
val databaseDriver = "org.postgresql.Driver"

val injectionModule = module {
    single { TwitterClient() }
    single { SearchCriteriaRepository() }
    single { TwitterService(get(), get()) }
}

val moshi: Moshi =  Moshi
    .Builder()
    .add(DateTimeAdapter())
    .add(UUIDAdapter())
    .build()


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.start() {
    executeMigrations()
    Database.connect(databaseUrl, databaseDriver, databaseUser, databasePassword)

    install(Koin) {
        modules(injectionModule)
    }

    install(ContentNegotiation) {
        moshi(moshi)
    }

    install(StatusPages) {
        exception<Throwable> {
            val message = it.message ?: "Unknown error"
            val code = when(it) {
                is JsonDataException, is BusinessRuleException -> HttpStatusCode.BadRequest
                is ResourceNotFoundException -> HttpStatusCode.NotFound
                else -> HttpStatusCode.InternalServerError
            }
            call.respond(code, Error(message, it.javaClass.simpleName))
        }
    }

    routing {
        routes()
    }
}

fun executeMigrations() =
    Flyway
        .configure()
        .dataSource(databaseUrl, databaseUser, databasePassword)
        .load()
        .migrate()
