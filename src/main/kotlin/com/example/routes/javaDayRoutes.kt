package com.example.routes

import com.example.twitter.TwitterClient
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.javaDayRoutes() = route("/twitter") {

    val client = TwitterClient()
    get("/") {
        val tweets = client.searchByDateAndMultipleHashtags()
        call.respond(tweets)
    }

    get("/top"){
        call.respond(client.count())
    }

}
