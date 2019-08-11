package com.example.service

import com.example.domain.SearchCriteria
import com.example.exception.ResourceNotFoundException
import com.example.repository.SearchCriteriaRepository
import com.example.twitter.TwitterClient
import com.squareup.moshi.JsonClass
import org.joda.time.LocalDateTime
import java.util.*

class PepeService(
    private val twitterClient: TwitterClient,
    private val searchCriteriaRepository: SearchCriteriaRepository
) {

    // TODO: How to test suspend functions
    fun createQuery(query: SearchCriteria) = searchCriteriaRepository.insertQuery(query)
        // TODO: Can I have service level transactions like Spring? How can we make the same on javascript?
        // TODO: Verify duplicated name

    //TODO: Buscar Hot Reload
    fun topTweeters(uuid: UUID): List<TopTweetResult> {
        val topTweetersQuery = searchCriteriaRepository.getQuery(uuid) ?: throw ResourceNotFoundException("Query not found")
        val retrievedTweets = twitterClient.searchByQuery(topTweetersQuery)
        //mostrar el intent una funcion
        //mover a una funcion
        return retrievedTweets
            .filter { topTweetersQuery.dateRange?.contains(it.tweetedDate) ?: true }
            .groupBy { it.userName }
            .map { TopTweetResult(it.key, it.value.size) }
    }
}

@JsonClass(generateAdapter = true)
data class TopTweetResult(val name: String, val count: Int)

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
