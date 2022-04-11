package com.example.basicweatherforecast.utils

import android.icu.text.SimpleDateFormat
import java.util.*

object DataFormatter {

    fun Long.toFormatDate(): String = run {
        val simpleDate = SimpleDateFormat("dd MMM hh:mm", Locale.US)
        simpleDate.format(Date(this * 1000))
    }
}