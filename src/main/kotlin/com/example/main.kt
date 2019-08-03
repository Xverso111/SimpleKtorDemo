package com.example

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

val databaseUser = "pepe"
val databasePassword = "12345678"
val databaseUrl = "jdbc:postgresql://localhost/tweet-query-db"

fun dataSource() =
    BasicDataSource().apply {
        driverClassName = "org.postgresql.Driver"
        url = databaseUrl
        username = databaseUser
        password = databasePassword
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
