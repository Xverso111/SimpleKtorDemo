package com.example.service

import com.example.domain.Tweet
import com.example.domain.TweetQuery
import com.example.twitter.TwitterClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedDeque

class QueueService(
    private val client: TwitterClient
) {
    private val queue = ConcurrentLinkedDeque<ExecuteQuery>()

    init {
        GlobalScope.launch {
            while (true) {
                if(queue.isNotEmpty()) {
                    val executeQuery = queue.pop()
                    val response = client.searchByQuery(executeQuery.query)
                    async { executeQuery.onComplete(response) }
                }
            }
        }
    }

    fun add(executeQuery: ExecuteQuery) = queue.add(executeQuery)
}

class ExecuteQuery(
    val query: TweetQuery,
    val onComplete: suspend (List<Tweet>) -> Unit
)
