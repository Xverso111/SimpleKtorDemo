package com.example.twitter

import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterFactory


class TwitterClient {
    private val twitter = TwitterFactory.getSingleton()
    // #JavaDayEcuador since:2019-07-22 until:2019-07-24
    // #JavaDayEcuador AND #JavaDayGuatemala since:2019-07-22 until:2019-07-25
    private val query = Query("#JavaDayEcuador -filter:retweets").apply {
        since("2019-07-22")
        until("2019-07-24")
        resultType(Query.ResultType.recent)
    }

    fun searchByHashTag(hashTag: String): List<Status> {
        val query = Query(hashTag)
        val result = twitter.search(query)

        return result.tweets
    }

    fun count(): Int {
//        val query = Query("#SimplificaciónDeTrámitesEc")
        query.count = 1000
        var result = twitter.search(query)
        val tweets = mutableListOf<Status>().apply { addAll(result.tweets) }
        println("****************size${tweets.size}")
        println("****************${result.count}")
        var contador = 0
        while (result.hasNext()) {
            contador++
            if (contador % 10 == 0) {
                println("---------------$contador")
            }
//            println("------------------------------ hay mas")
            val nextQuery = result.nextQuery()
            result = twitter.search(nextQuery)
            tweets.addAll(result.tweets)
        }
        println("-------------------------------- moriste ${tweets.size}")
        return tweets.size
    }

    fun searchByDateAndMultipleHashtags(): List<Status> {
//        val query = Query("#JavaDayEcuador AND #GroundbreakersTour -filter:retweets")
//        val query = Query("#CasaParaTodos")

        //query.since("2019-07-22")
//        query.until("2019-07-24")
        //query.resultType(Query.ResultType.recent)
        val result = twitter.search(query)

        return result.tweets
    }

}


