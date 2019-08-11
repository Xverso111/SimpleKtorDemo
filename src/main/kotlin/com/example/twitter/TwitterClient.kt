package com.example.twitter

import com.example.domain.Tweet
import com.example.domain.SearchCriteria
import org.joda.time.LocalDateTime
import twitter4j.Status
import twitter4j.TwitterFactory


class TwitterClient {
    private val twitter = TwitterFactory.getSingleton()

    // TODO: Esto debería lanzarse en una coroutine. Porque puede tomar mucho tiempo
    // TODO: El resultado debería ser guardado en algún lado para luego ser consultado
    // TODO: 180 requests cada 15 mins -> 1 request every 5 seconds
    fun searchByQuery(searchCriteria: SearchCriteria):List<Tweet> {
        // TODO: Hacer esto más funcional?
        // TODO: Put a 5/3 seconds delay?
        val query = searchCriteria.toQuery()
        var result = twitter.search(query)
        val tweets: MutableList<Status> = result.tweets
        while (result.hasNext()) {
            val nextQuery = result.nextQuery()
            result = twitter.search(nextQuery)
            tweets.addAll(result.tweets)
        }
        return tweets.map(Status::toTweet)
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


