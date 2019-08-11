package com.example.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Error(val message: String, val exception: String)
