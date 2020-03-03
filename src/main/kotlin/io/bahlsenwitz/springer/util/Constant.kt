package io.bahlsenwitz.springer.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Constant {
    val INFLUX: String = "http://localhost:8086/"

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val brooklyn = ZoneId.of("America/New_York")

    fun getDate(): String {
        return formatter.format(ZonedDateTime.now(brooklyn)).toString()
    }

    fun getDate(string: String): ZonedDateTime {
        return LocalDateTime.parse(string, formatter).atZone(brooklyn)
    }
}