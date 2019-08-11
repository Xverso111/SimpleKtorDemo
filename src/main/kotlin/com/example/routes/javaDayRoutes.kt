package com.example.routes

import com.example.domain.SearchCriteria
import com.example.exception.BusinessRuleException
import com.example.service.PepeService
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

    // TODO: Test as a compiled jar
    // TODO: SERDES -> Moshi
    // TODO: Pasar Twitter4J a builder
    // TODO: Usar KConfig para las env variables
    // TODO: JWT Authorization
    // TODO: Persistencia
    // TODO: inyeccion de dependencias
    // TODO: Set up exception handling -> StatusPages
    val service: PepeService by inject()

    post("/query") {
        val searchCriteria = call.receive<SearchCriteria>()
        val id = service.createQuery(searchCriteria)
        // TODO: Should we return just the ID or the whole object with the id? -> Check Rest standards
        call.respond(HttpStatusCode.OK, IdResponse(id))
    }

    get("/query/{id}/top") {
        // TODO: Maybe command is not the name?
        // TODO: Get rid of the command?
        val uuid = call.parameters["id"].validUUID("The id is not valid")
        val topTweeters = service.topTweeters(uuid)
        call.respond(HttpStatusCode.OK, topTweeters)
    }
}

fun String?.validUUID(message: String) =
    try {
        UUID.fromString(this)
    } catch (exception: Exception) {
        // TODO: Probably use some custom exceptions that represent our domain?
        throw BusinessRuleException(message)
    }

data class IdResponse(val id: UUID)
