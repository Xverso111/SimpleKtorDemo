package com.example.service

import com.example.domain.TweetQuery
import com.example.twitter.TwitterClient
import java.time.LocalDateTime
import java.util.*

class PepeService(
    val twitterClient: TwitterClient
) {

    fun topTweeters(dateRange: DateRange? = null): Map<String, Int> {
        val resultsMap = mutableMapOf<String, Int>()
        val topTweetersQuery = TweetQuery(
            UUID.randomUUID(),
            "topTweeters",
            listOf("JavaDayEcuador", "nuestro"),
            dateRange
        )
        val retrievedTweets = twitterClient.searchByQuery(topTweetersQuery)

        val filteredTweetsByDate = if (dateRange != null) {
            retrievedTweets.filter { dateRange contains it.tweetedDate }
        } else retrievedTweets

        filteredTweetsByDate.forEach {
            resultsMap[it.userName] = resultsMap[it.userName]?.plus(1) ?: 1
        }

        // Filtrar por horas

        return resultsMap
    }
}

// TODO: usar un Object
// TODO: data class -> overrides equals/hashcode/toString...
data class DateRange(
    private val start: LocalDateTime,
    private val end: LocalDateTime
) {

    // validar from is before to

    infix fun contains(date: LocalDateTime) = date.isBetween(start, end)

    private fun LocalDateTime.isBetween(start: LocalDateTime, end: LocalDateTime) = this.isAfterOrEqual(start) && this.isBeforeOrEqual(end)
    private fun LocalDateTime.isAfterOrEqual(date: LocalDateTime) = this.isAfter(date) || this.isEqual(date)
    private fun LocalDateTime.isBeforeOrEqual(date: LocalDateTime) = this.isBefore(date) || this.isEqual(date)
}

// TODO: Mencionar que tal ves sea mejor devolver un data class con el nombre de usuario, Id y acumulado
// y deveriamos acumular por id y no por username
