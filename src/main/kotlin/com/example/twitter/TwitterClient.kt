package com.example.twitter

import com.example.domain.Tweet
import org.joda.time.LocalDateTime
import twitter4j.*


class TwitterClient {
    private val twitter = TwitterFactory.getSingleton()

    // TODO: Esto debería lanzarse en una coroutine. Porque puede tomar mucho tiempo
    // TODO: El resultado debería ser guardado en algún lado para luego ser consultado
    // TODO: 180 requests cada 15 mins -> 1 request every 5 seconds
    fun searchByQuery(query: Query) =
        twitter
            .query(query)
            .flatMap { it.tweets }
            .map { it.toTweet() }

}

fun Twitter.query(query: Query) = Iterable { TwitterQueryIterator(this, query) }

class TwitterQueryIterator(
    private val twitter: Twitter,
    query: Query
): Iterator<QueryResult> {

    private var currentQuery: Query? = query

    override fun hasNext() = currentQuery != null

    override fun next(): QueryResult {
        val queryResult = twitter.search(currentQuery)
        currentQuery = queryResult.nextQuery()
        return queryResult
    }
}

//TODO: Explicar esta cosa
// TODO: Test this
private fun Status.toTweet() =
    Tweet(
        this.id,
        this.user.screenName,
        this.text,
        LocalDateTime(this.createdAt.toInstant().toEpochMilli())
    )


