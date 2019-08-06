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
import org.koin.ktor.ext.inject
import java.util.*

fun Route.javaDayRoutes() = route("/twitter") {

    // TODO: SERDES -> Moshi
    // TODO: Pasar Twitter4J a builder
    // TODO: Usar KConfig para las env variables
    // TODO: JWT Authorization
    // TODO: Persistencia
    // TODO: inyeccion de dependencias
    // TODO: Set up exception handling -> StatusPages
    val client: TwitterClient by inject()
    val queryRepository: QueryRepository by inject()
    val service = PepeService(client, queryRepository)
    get("/") {
        val tweets = client.searchByDateAndMultipleHashtags()
        call.respond(tweets)
    }

    post("/query") {
        val query = call.receive<TweetQuery>()
        service.createQuery(query)
        // TODO: Should we return just the ID or the whole object with the id? -> Check Rest standards
        call.respond(HttpStatusCode.OK, IdResponse(query.id))
    }

    get("/query/{id}/top") {
        // TODO: Maybe command is not the name?
        val command = UUIDCommand(call.parameters["id"])
        val topTweeters = service.topTweeters(command).map { it.toPair() }.sortedByDescending { it.second }
        call.respond(HttpStatusCode.OK, topTweeters)
    }
}

data class IdResponse(val id: UUID)
