package com.example.routes

import com.example.domain.SearchCriteria
import com.example.dto.IdResponse
import com.example.exception.BusinessRuleException
import com.example.service.TwitterService
import com.example.validation.validUUID
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

fun Route.routes() = route("/twitter/search") {

    // TODO: Test as a compiled jar
    // TODO: SERDES -> Moshi
    // TODO: Pasar Twitter4J a builder
    // TODO: Usar KConfig para las env variables
    // TODO: JWT Authorization
    // TODO: Persistencia
    // TODO: inyeccion de dependencias
    // TODO: Set up exception handling -> StatusPages
    val service: TwitterService by inject()

    post {
        val searchCriteria = call.receive<SearchCriteria>()
        val id = service.createQuery(searchCriteria)
        call.respond(HttpStatusCode.OK, IdResponse(id))
    }

    get("/{id}/top") {
        val uuid = call.parameters["id"].validUUID("The id is not valid")
        val topTweeters = service.topTweeters(uuid)
        call.respond(HttpStatusCode.OK, topTweeters)
    }
}

