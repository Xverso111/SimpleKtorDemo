package com.example.command

import java.util.*

class UUIDCommand(
    uuid: String?
) {
    val uuid: UUID = uuid
        .validUUID("The id is not valid")
}

fun String?.validUUID(message: String) =
    try {
        UUID.fromString(this)
    } catch (exception: Exception) {
        // TODO: Probably use some custom exceptions that represent our domain?
        throw Exception(message)
    }
