package com.example.adapter

import com.example.domain.SearchCriteria
import twitter4j.Query

class QueryAdapter(
    searchCriteria: SearchCriteria
) {
    private val filter = if(searchCriteria.allowRetweets) "" else "-filter:retweets"
    private val hashTags = searchCriteria.hashTags.map { "#$it" }.reduce { acc, hashtag -> "$acc AND $hashtag" }

    val query = Query("$hashTags $filter").apply {
        searchCriteria.dateRange?.let {
            since = it.start.toString("YYYY-MM-dd")
            until = it.end.toString("YYYY-MM-dd")
        }
    }
}
