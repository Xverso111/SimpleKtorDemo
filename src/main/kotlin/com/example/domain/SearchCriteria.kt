package com.example.domain

import com.example.validation.notBlank
import com.example.validation.notEmpty
import com.squareup.moshi.JsonClass

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
}
