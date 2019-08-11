package com.example.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopTweetResult(val name: String, val count: Int)
