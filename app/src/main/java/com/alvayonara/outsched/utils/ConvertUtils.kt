package com.alvayonara.outsched.utils

import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

object ConvertUtils {

    fun convertTimeToDate(time: Long?): String? {
        // assign empty value for convertResult
        var convertResult = ""

        try {
            val timeUnixToDate = Date(time!! * 1000)

            // reformat date time style (ex: December 31, 1997)
            val formatterReformat =
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            // convert result
            convertResult = formatterReformat.format(timeUnixToDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertResult
    }

    fun convertTimeToHour(time: Long?): String? {
        // assign empty value for convertResult
        var convertResult = ""

        try {
            val timeUnixToDate = Date(time!! * 1000)

            // reformat to hour (ex: 17:00)
            val formatterReformat =
                SimpleDateFormat("HH:mm", Locale.getDefault())

            // convert result
            convertResult = formatterReformat.format(timeUnixToDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertResult
    }

    fun convertTemperatureRound(temperature: Double?): Int?{
        // assign empty value for convertResult
        var convertResult = 0

        try {
            // convert result
            convertResult = DecimalFormat("0.#").format(ceil(temperature!!)).toInt()
        } catch (e: Exception){
            e.printStackTrace()
        }

        return convertResult
    }
}