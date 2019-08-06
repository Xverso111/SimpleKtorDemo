package com.example.service

import com.example.command.UUIDCommand
import com.example.domain.TweetQuery
import com.example.repository.QueryRepository
import com.example.twitter.TwitterClient
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.LocalDateTime
import java.util.*

class PepeService(
    private val twitterClient: TwitterClient,
    private val queryRepository: QueryRepository
) {

    // TODO: How to test suspend functions
    suspend fun createQuery(query: TweetQuery) {
        // TODO: Can I have service level transactions like Spring? How can we make the same on javascript?
        // TODO: Verify duplicated name
        queryRepository.insertQuery(query)
    }

    //TODO: Buscar Hot Reload
    suspend fun topTweeters(command: UUIDCommand): Map<String, Int> {
        val resultsMap = mutableMapOf<String, Int>()
        val topTweetersQuery = queryRepository.getQuery(command.uuid) ?: throw Exception("Query not found")
        val retrievedTweets = twitterClient.searchByQuery(topTweetersQuery)
        val filteredTweetsByDate = if (topTweetersQuery.dateRange != null) {
            retrievedTweets.filter { topTweetersQuery.dateRange contains it.tweetedDate }
        } else retrievedTweets

        filteredTweetsByDate.forEach {
            resultsMap[it.userName] = resultsMap[it.userName]?.plus(1) ?: 1
        }
        return resultsMap
    }
}

// TODO: usar un Object
// TODO: data class -> overrides equals/hashcode/toString...
@JsonClass(generateAdapter = true)
data class DateRange(
    val start: LocalDateTime,
    val end: LocalDateTime
) {

    // validar from is before to

    infix fun contains(date: LocalDateTime) = date.isBetween(start, end)

    private fun LocalDateTime.isBetween(start: LocalDateTime, end: LocalDateTime) = this.isAfterOrEqual(start) && this.isBeforeOrEqual(end)
    private fun LocalDateTime.isAfterOrEqual(date: LocalDateTime) = this.isAfter(date) || this.isEqual(date)
    private fun LocalDateTime.isBeforeOrEqual(date: LocalDateTime) = this.isBefore(date) || this.isEqual(date)
}

// TODO: Mencionar que tal ves sea mejor devolver un data class con el nombre de usuario, Id y acumulado
// y deveriamos acumular por id y no por username
