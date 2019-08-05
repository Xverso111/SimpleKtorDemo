package com.example.routes

import com.example.command.UUIDCommand
import com.example.domain.TweetQuery
import com.example.repository.QueryRepository
import com.example.service.PepeService
import com.example.twitter.TwitterClient
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import kotlinx.coroutines.async
import java.util.*

fun Route.javaDayRoutes() = route("/twitter") {

    // TODO: SERDES -> Moshi
    // TODO: Pasar Twitter4J a builder
    // TODO: Usar KConfig para las env variables
    // TODO: JWT Authorization
    // TODO: Persistencia
    // TODO: inyeccion de dependencias
    // TODO: Set up exception handling -> StatusPages
    val client = TwitterClient()
    val queryRepository = QueryRepository()
    val service = PepeService(client, queryRepository)
    get("/") {
        val tweets = client.searchByDateAndMultipleHashtags()
        call.respond(tweets)
    }

    post("/query") {
        val query = call.receive<TweetQuery>()
        service.createQuery(query)
        // TODO: Should we return just the ID or the whole object with the id?
        call.respond(HttpStatusCode.OK, IdResponse(query.id))
    }

    get("/query/{id}/top"){
        val command = UUIDCommand(call.parameters["id"])
        val topTweeters = async { service.topTweeters(command) } //.map { it.toPair()}.sortedByDescending { it.second }
        println("Ya respondí")
        call.respond(HttpStatusCode.OK)
    }
}

data class IdResponse(val id: UUID)
