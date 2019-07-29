package com.example.service

import com.example.domain.TweetQuery
import com.example.twitter.TwitterClient
import java.time.LocalDateTime
import java.util.*

class PepeService(val twitterClient: TwitterClient) {

    fun topTweeters(): Map<Long, Int> {
        val resultsMap = mutableMapOf<Long, Int>()
        val topTweetersQuery = TweetQuery(
            UUID.randomUUID(),
            "topTweeters",
            listOf("JavaDayEcuador", "nuestro"),
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now()
        )

        twitterClient.searchByQuery(topTweetersQuery).forEach {
            resultsMap[it.userId] = resultsMap[it.userId]?.plus(1) ?: 1 // TODO:horas en explicar
        }

        return resultsMap
    }
}
