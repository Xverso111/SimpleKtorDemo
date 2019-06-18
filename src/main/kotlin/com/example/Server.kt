package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

class Server {

    val module = fun Application.(){
        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter())
            register(ContentType.Text.Plain, GsonConverter())
        }

        routing {
            get("/") {
                call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
            }

            // Static feature. Try to access `/static/ktor_logo.svg`
            static("/static") {
                resources("static")
            }

            get("/json/gson") {
                call.respond(mapOf("hello" to "world"))
            }
        }

    }

    fun create(): NettyApplicationEngine {
        return embeddedServer(Netty, 8089) { module() }
    }

}
