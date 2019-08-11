package com.example.domain

import org.joda.time.LocalDateTime

//TODO: ponerle un extension a la clase Status de la libreria del cliente de twitter hecha en java
//TODO: preguntarle el rango
class Tweet(
    val userId: Long,
    val userName: String,
    val text: String,
    val tweetedDate: LocalDateTime
)
