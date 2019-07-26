package com.example.domain

import twitter4j.Query
import java.time.LocalDateTime
import java.util.*

//TODO:completar validaciones o reglas de negocio y escribir las pruebas
class TweetQuery(
    val id: UUID,
    nombre: String,
    hashTags: List<String>,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val allowRetweets: Boolean = false
) {
    val hashTags = hashTags
        .notEmpty("Cannot create a TweetQuery without hashtags")

    val nombre = nombre
        .notBlank("Cannot create a TweetQuery without a name")

    fun toQuery(): Query {
        val queryString = hashTags.reduce { acc, hashtag -> "$acc AND $hashtag" }
        return Query(queryString)
    }
}

fun <T> List<T>.notEmpty(message: String) =
    this.takeIf { it.isNotEmpty() } ?: throw Exception(message)

fun String.notBlank(message: String) =
    this.takeIf { it.isNotBlank() } ?: throw Exception(message)
