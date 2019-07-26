package com.example.domain

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.*

class TweetQueryTest {

    @Test
    fun `should throw an error when creating a TweetQuery without hashtags`() {
        assertThatThrownBy {
            TweetQuery(
                UUID.randomUUID(),
                "name",
                emptyList(),
                allowRetweets = false
            )
        }.hasMessage("Cannot create a TweetQuery without hashtags")
    }

    @Test
    internal fun `should return a Query`() {
        println(TweetQuery(UUID.randomUUID(), "name", listOf("ss", "dc")).toQuery().query)
    }
}
