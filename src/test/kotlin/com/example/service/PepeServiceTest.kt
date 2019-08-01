package com.example.service

import com.example.domain.Tweet
import com.example.twitter.TwitterClient
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.apache.commons.lang3.RandomUtils.nextLong
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

//TODO: la primera ves que usemos un list of comentar que es mas legible que el metodo of que tiene java 9
class PepeServiceTest {

    @Test
    fun `should return a map of tweets`() {
        val twitterClient = mockk<TwitterClient>()
        val tweet = randomTweet()
        val expectedTable = mapOf(tweet.userName to 1)

        every { twitterClient.searchByQuery(any()) } returns listOf(tweet)

        val topTable = PepeService(twitterClient).topTweeters()

        assertThat(topTable).containsAllEntriesOf(expectedTable)
    }

    @Test
    fun `should return accumulated tweets`() {
        val twitterClient = mockk<TwitterClient>()
        val tweet = randomTweet()
        val secondTweet = randomTweet()

        val expectedTable = mapOf(
            tweet.userName to 2,
            secondTweet.userName to 1
        )

        every { twitterClient.searchByQuery(any()) } returns listOf(tweet, tweet, secondTweet)

        val topTable = PepeService(twitterClient).topTweeters()

        assertThat(topTable).containsAllEntriesOf(expectedTable)
    }

    @Test
    fun `should return tweets that are inside the specified time range`() {
        val now = LocalDateTime.now()
        val nowMinus10Mins = LocalDateTime.now().minusMinutes(10)
        val nowPlus10Mins = LocalDateTime.now().plusMinutes(10)
        val twitterClient = mockk<TwitterClient>()
        val tweetInsideRange = randomTweet(tweetDate = now)
        val tweetBelowRange = randomTweet(tweetDate = nowMinus10Mins.minusNanos(1))
        val tweetAboveRange = randomTweet(tweetDate = nowPlus10Mins.plusNanos(1))
        val dateRange = DateRange(nowMinus10Mins, nowPlus10Mins)

        every { twitterClient.searchByQuery(any()) } returns listOf(tweetInsideRange, tweetBelowRange, tweetAboveRange)

        val topTable = PepeService(twitterClient).topTweeters(dateRange)

        assertThat(topTable).doesNotContainKeys(tweetBelowRange.userName, tweetAboveRange.userName)
        assertThat(topTable).containsKeys(tweetInsideRange.userName)
    }

    private fun randomTweet(
        userId: Long = nextLong(1, 100),
        userName: String = randomAlphabetic(10),
        text: String = randomAlphanumeric(140),
        tweetDate: LocalDateTime = LocalDateTime.now()
    ) = Tweet(userId, userName, text, tweetDate)

}

// TODO: lo facil que es cambiar el tipado de las variables sin tener que cambiar su declaracion
