package com.swtug.anticovid.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    fun getLocalDateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    }

    fun getStringFromDate(date: LocalDateTime): String {
        return date.format(getLocalDateTimeFormatter())
    }

    fun getDateFromString(dateText: String): LocalDateTime {
        return LocalDateTime.parse(dateText, getLocalDateTimeFormatter())
    }
}