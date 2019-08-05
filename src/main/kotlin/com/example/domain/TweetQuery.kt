package com.example.domain

import com.example.service.DateRange
import twitter4j.Query
import java.util.*

//TODO:completar validaciones o reglas de negocio y escribir las pruebas
//TODO: change the name of this class -> when colliding with Twitter4j query use aliases
class TweetQuery(
    val id: UUID = UUID.randomUUID(),
    name: String,
    hashTags: List<String>,
    val dateRange: DateRange? = null,
    val allowRetweets: Boolean = false
) {
    // TODO: Validate name length -> max 50 chars
    // TODO: Hashtags shouldn't include #, should be manually put

    val hashTags = hashTags
        .notEmpty("Cannot create a TweetQuery without hashTags")

    val nombre = name
        .notBlank("Cannot create a TweetQuery without a name")

    fun toQuery(): Query {
        val queryString = hashTags
            .reduce { acc, hashtag -> "$acc AND $hashtag" }
            .plus(if(allowRetweets) "-filter:retweets" else "")

        return Query(queryString).apply {
            dateRange?.let {
                since = it.start.toString()
                until = it.end.toString()
            }
        }
    }
}

fun <T> List<T>.notEmpty(message: String) =
    this.takeIf { it.isNotEmpty() } ?: throw Exception(message)

fun String.notBlank(message: String) =
    this.takeIf { it.isNotBlank() } ?: throw Exception(message)
