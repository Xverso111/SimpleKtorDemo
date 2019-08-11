package com.example.domain

import com.example.exception.BusinessRuleException
import com.example.service.DateRange
import com.squareup.moshi.JsonClass
import twitter4j.Query
import java.util.*

//TODO:completar validaciones o reglas de negocio y escribir las pruebas
//TODO: change the name of this class -> when colliding with Twitter4j query use aliases
//TODO: Search Criteria
@JsonClass(generateAdapter = true)
class SearchCriteria(
    name: String,
    hashTags: List<String>,
    val dateRange: DateRange? = null,
    val allowRetweets: Boolean = false
) {
    // TODO: Validate name length -> max 50 chars
    // TODO: Hashtags shouldn't include #, should be manually put

    val hashTags = hashTags
        .notEmpty("Cannot create a SearchCriteria without hashTags")

    val name = name
        .notBlank("Cannot create a SearchCriteria without a name")

    //String "#Javaday AND "
    //TODO: separar esto a un adapter
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
    this.takeIf { it.isNotEmpty() } ?: throw BusinessRuleException(message)

fun String.notBlank(message: String) =
    this.takeIf { it.isNotBlank() } ?: throw BusinessRuleException(message)
