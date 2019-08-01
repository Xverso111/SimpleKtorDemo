package com.example.service

import com.example.domain.TweetQuery
import com.example.twitter.TwitterClient
import java.time.LocalDateTime
import java.util.*

class PepeService(val twitterClient: TwitterClient) {

    fun topTweeters(): Map<String, Int> {
        val resultsMap = mutableMapOf<String, Int>()
        val topTweetersQuery = TweetQuery(
            UUID.randomUUID(),
            "topTweeters",
            listOf("JavaDayEcuador", "nuestro"),
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now()
        )

        twitterClient.searchByQuery(topTweetersQuery).forEach {
            resultsMap[it.userName] = resultsMap[it.userName]?.plus(1) ?: 1 // TODO:horas en explicar
        }

        // Filtrar por horas

        return resultsMap
    }
}

// TODO: Mencionar que tal ves sea mejor devolver un data class con el nombre de usuario, Id y acumulado
// y deveriamos acumular por id y no por username
