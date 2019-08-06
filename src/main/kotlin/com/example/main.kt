package com.example

import com.example.repository.QueryRepository
import com.example.twitter.TwitterClient
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val databaseUser = "pepe"
val databasePassword = "12345678"
val databaseUrl = "jdbc:postgresql://localhost/tweet-query-db"


// TODO: Hacer como hicimos en Delaware
fun dataSource() =
    BasicDataSource().apply {
        driverClassName = "org.postgresql.Driver"
        url = databaseUrl
        username = databaseUser
        password = databasePassword
    }

val injectionModule = module {
    single { TwitterClient() }
    single { QueryRepository() }
}


fun main() {
    executeMigrations()
    Database.connect(dataSource())
    Server().create().start(wait = true)
}

fun executeMigrations() =
    Flyway
        .configure()
        .dataSource(databaseUrl, databaseUser, databasePassword)
        .load()
        .migrate()
