package com.example.service

import com.example.domain.TweetQuery
import java.time.LocalDateTime
import java.util.*

class PepeService (){

    fun topTweeters(){
        //crea el tweet query
        //consulta
        val topTweetersQuery = TweetQuery(
            UUID.randomUUID(),
            "topTweeters",
            listOf("JavaDayEcuador", "nuestro"),
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now()
        )
        val tweets = topTweetersQuery
    }
}
