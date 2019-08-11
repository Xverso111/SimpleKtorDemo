package com.example.domain

import com.squareup.moshi.JsonClass
import org.joda.time.LocalDateTime

@JsonClass(generateAdapter = true)
class DateRange(
    val start: LocalDateTime,
    val end: LocalDateTime
) {

    // validar from is before to

    infix fun contains(date: LocalDateTime) = date.isBetween(start, end)

    private fun LocalDateTime.isBetween(start: LocalDateTime, end: LocalDateTime) = this.isAfterOrEqual(start) && this.isBeforeOrEqual(end)
    private fun LocalDateTime.isAfterOrEqual(date: LocalDateTime) = this.isAfter(date) || this.isEqual(date)
    private fun LocalDateTime.isBeforeOrEqual(date: LocalDateTime) = this.isBefore(date) || this.isEqual(date)
}
