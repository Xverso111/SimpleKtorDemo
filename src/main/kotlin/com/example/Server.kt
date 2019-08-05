package com.example

//import io.ktor.gson.GsonConverter
import com.example.routes.javaDayRoutes
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.ryanharter.ktor.moshi.moshi
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.util.*


class Server {

    val module = fun Application.() {
        install(ContentNegotiation) {
//            register(ContentType.Application.Json, GsonConverter())
//            register(ContentType.Text.Plain, GsonConverter())
            // TODO: try to use MOSHI? Or some kind of middle data class (proxy)
//            moshi {
//                add()
//            }
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                setTimeZone(TimeZone.getTimeZone("America/Guayaquil"))
                registerModule(JodaModule())
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
