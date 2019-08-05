package com.example

import com.example.routes.javaDayRoutes
import com.example.serializer.DateTimeAdapter
import com.example.serializer.UUIDAdapter
import com.ryanharter.ktor.moshi.moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine


class Server {

    val module = fun Application.() {
        install(ContentNegotiation) {
            moshi {
                add(DateTimeAdapter())
                add(UUIDAdapter())
                add(KotlinJsonAdapterFactory())
            }
        }

        routing {
            get("/test"){
                call.respondText("hello world!", contentType = ContentType.Text.Plain)
            }
            javaDayRoutes()
        }
    }

    fun create(): NettyApplicationEngine {
        return embeddedServer(Netty, 8089, "127.0.0.1") { module() }
    }

}
