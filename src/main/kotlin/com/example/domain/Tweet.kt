package com.example.domain

import java.time.LocalDateTime

//TODO: ponerle un extension a la clase Status de la libreria del cliente de twitter hecha en java

data class Tweet (val userId: Long, val userName:String, val text:String, val tweetedDate:LocalDateTime)
