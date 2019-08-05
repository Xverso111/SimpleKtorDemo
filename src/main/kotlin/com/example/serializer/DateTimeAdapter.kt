package com.example.serializer

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.ToJson
import org.joda.time.LocalDateTime
import org.joda.time.format.ISODateTimeFormat


class DateTimeAdapter {

    @ToJson
    fun toJson(datetime: LocalDateTime) = ISODateTimeFormat.dateTime().print(datetime)

    @FromJson
    fun fromJson(datetime: String) = try {
        ISODateTimeFormat
            .dateTimeParser()
            .parseDateTime(datetime)
            .toLocalDateTime()

    } catch (exception: Exception) {
        throw JsonDataException("Incorrect datetime")
    }
}
