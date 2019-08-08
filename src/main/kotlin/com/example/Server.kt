package com.example

import com.example.exception.BusinessRuleException
import com.example.exception.ResourceNotFoundException
import com.example.routes.javaDayRoutes
import com.example.serializer.DateTimeAdapter
import com.example.serializer.UUIDAdapter
import com.ryanharter.ktor.moshi.moshi
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import org.koin.ktor.ext.Koin

val moshi: Moshi =  Moshi
    .Builder()
    .add(DateTimeAdapter())
    .add(UUIDAdapter())
    .build()

class Server {

    val module = fun Application.() {
        install(Koin) {
            modules(injectionModule)
        }
        install(ContentNegotiation) {
            moshi(moshi)
        }

        install(StatusPages) {
            exception<Exception> {
                val code = when(it) {
                    is JsonDataException, is BusinessRuleException -> HttpStatusCode.BadRequest
                    is ResourceNotFoundException -> HttpStatusCode.NotFound
                    else -> HttpStatusCode.InternalServerError
                }
                call.respond(code, Error(it.message ?: "Unknown error", it.javaClass.simpleName))
            }
        }

        routing {
            get("/test") {
                call.respondText("hello world!", contentType = ContentType.Text.Plain)
            }
            javaDayRoutes()
        }
    }

    fun create(): NettyApplicationEngine {
        return embeddedServer(Netty, 8089, "127.0.0.1") { module() }
    }

}

@JsonClass(generateAdapter = true)
data class Error(val message: String, val exception: String)
