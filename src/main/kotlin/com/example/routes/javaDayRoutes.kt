package com.example.routes

import com.example.service.PepeService
import com.example.twitter.TwitterClient
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.javaDayRoutes() = route("/twitter") {

    // TODO: JWT Authorization
    // TODO: Persistencia
    // TODO: inyeccion de dependencias
    val client = TwitterClient()
    val service = PepeService(client)
    get("/") {
        val tweets = client.searchByDateAndMultipleHashtags()
        call.respond(tweets)
    }

    get("/top"){
        val topTweeters = service.topTweeters(null).map { it.toPair()}.sortedByDescending { it.second }
        call.respond(topTweeters)
    }

}
