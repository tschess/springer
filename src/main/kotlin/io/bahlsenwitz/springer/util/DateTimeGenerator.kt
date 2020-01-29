package io.bahlsenwitz.springer.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateTimeGenerator {

    private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")
    private val BROOKLYN = ZoneId.of("America/New_York")

    fun rightNowString(): String {
        return FORMATTER.format(ZonedDateTime.now(BROOKLYN))
    }

    fun rightNowZone(): ZonedDateTime {
        return ZonedDateTime.now(BROOKLYN)
    }

    fun getZone(date: String): ZonedDateTime {
        val dateTimeFormatted = LocalDateTime.parse(date, FORMATTER)
        return ZonedDateTime.of(dateTimeFormatted, BROOKLYN)
    }

}