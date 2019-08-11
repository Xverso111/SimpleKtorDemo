package com.example.service

import com.example.adapter.QueryAdapter
import com.example.domain.SearchCriteria
import com.example.dto.TopTweetResult
import com.example.exception.ResourceNotFoundException
import com.example.repository.SearchCriteriaRepository
import com.example.twitter.TwitterClient
import java.util.*

class TwitterService(
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
        // TODO: Is this adapter correct, should it be a class or a function?
        val retrievedTweets = twitterClient.searchByQuery(QueryAdapter(topTweetersQuery).query)
        //mostrar el intent una funcion
        //mover a una funcion
        return retrievedTweets
            .filter { topTweetersQuery.dateRange?.contains(it.tweetedDate) ?: true }
            .groupBy { it.userName }
            .map { TopTweetResult(it.key, it.value.size) }
    }
}

// TODO: Mencionar que tal ves sea mejor devolver un data class con el nombre de usuario, Id y acumulado
// y deveriamos acumular por id y no por username
