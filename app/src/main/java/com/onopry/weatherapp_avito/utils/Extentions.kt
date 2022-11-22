package com.onopry.weatherapp_avito.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.onopry.domain.model.forecast.Daily
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.presentation.model.DailyUi
import okhttp3.OkHttpClient
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

fun OkHttpClient.Builder.addQueryParam(
    name: String,
    key: String,
): OkHttpClient.Builder = this.addInterceptor { chain ->
    val request = chain.request()
    val url = request
        .url
        .newBuilder()
        .addQueryParameter(name, key)
        .build()
    val newRequest = request.newBuilder().url(url).build()
    chain.proceed(newRequest)
}

fun Fragment.shortToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun View.show() = apply { if (visibility != View.VISIBLE) visibility = View.VISIBLE }

fun View.hide() = apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }

fun View.gone() = apply { if (visibility != View.GONE) visibility = View.GONE }

inline fun View.showIfConditionOrHide(condition: () -> Boolean) = apply {
    visibility = if (condition() && visibility != View.VISIBLE) View.VISIBLE else View.INVISIBLE
}

inline fun View.showIfConditionOrGone(condition: () -> Boolean) = apply {
    visibility = if (condition() && visibility != View.VISIBLE) View.VISIBLE else View.GONE
}

fun Any?.isNotNull() = this != null

fun LocalDate.getMonthName(fullName: Boolean) =
    if (fullName) month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) ?: "Month err"
    else month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) ?: "Month err"


fun LocalDate.getDayOfWeekName(fullName: Boolean) =
    if (fullName) dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH) ?: "Day err"
    else dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) ?: "Day err"

fun LocalDate.getDayOfWeekDayMonth(fullMonth: Boolean, fullDay: Boolean): String {
    val day = getDayOfWeekName(fullName = fullDay)
    val number = dayOfMonth
    val month = getMonthName(fullMonth)
    return "$day, $number $month"
}
//fun LocalDate.getCurrentDate

fun ImageView.setImageByWeatherCode(code: Int) {
    when (code) {
        0 -> setImageResource(R.drawable.wmo_0)

        1 -> setImageResource(R.drawable.wmo_1)
        2 -> setImageResource(R.drawable.wmo_2)
        3 -> setImageResource(R.drawable.wmo_3)

        45 -> setImageResource(R.drawable.wmo_45)
        48 -> setImageResource(R.drawable.wmo_48)

        51 -> setImageResource(R.drawable.wmo_51_61)
        53 -> setImageResource(R.drawable.wmo_53_63)
        55 -> setImageResource(R.drawable.wmo_55_65)

        56 -> setImageResource(R.drawable.wmo_56)
        57 -> setImageResource(R.drawable.wmo_57)

        61 -> setImageResource(R.drawable.wmo_51_61)
        63 -> setImageResource(R.drawable.wmo_53_63)
        65 -> setImageResource(R.drawable.wmo_55_65)

        66 -> setImageResource(R.drawable.wmo_66)
        67 -> setImageResource(R.drawable.wmo_67)

        71 -> setImageResource(R.drawable.wmo_71)
        73 -> setImageResource(R.drawable.wmo_73)
        75 -> setImageResource(R.drawable.wmo_75_77)
        77 -> setImageResource(R.drawable.wmo_75_77)

        80 -> setImageResource(R.drawable.wmo_80)
        81 -> setImageResource(R.drawable.wmo_81)
        82 -> setImageResource(R.drawable.wmo_82)

        85 -> setImageResource(R.drawable.wmo_85)
        86 -> setImageResource(R.drawable.wmo_86)

        95 -> setImageResource(R.drawable.wmo_95)

        96 -> setImageResource(R.drawable.wmo_96)
        99 -> setImageResource(R.drawable.wmo_99)
    }
}

fun TextView.setDescriptionByWeatherCode(code: Int) {
    when (code) {
        0 -> setText(R.string.wmo_0)

        1 -> setText(R.string.wmo_1)
        2 -> setText(R.string.wmo_2)
        3 -> setText(R.string.wmo_3)

        45 -> setText(R.string.wmo_45)
        48 -> setText(R.string.wmo_48)

        51 -> setText(R.string.wmo_51)
        53 -> setText(R.string.wmo_53)
        55 -> setText(R.string.wmo_55)

        56 -> setText(R.string.wmo_56)
        57 -> setText(R.string.wmo_57)

        61 -> setText(R.string.wmo_61)
        63 -> setText(R.string.wmo_53)
        65 -> setText(R.string.wmo_65)

        66 -> setText(R.string.wmo_66)
        67 -> setText(R.string.wmo_67)

        71 -> setText(R.string.wmo_71)
        73 -> setText(R.string.wmo_73)
        75 -> setText(R.string.wmo_75)
        77 -> setText(R.string.wmo_77)

        80 -> setText(R.string.wmo_80)
        81 -> setText(R.string.wmo_71)
        82 -> setText(R.string.wmo_82)

        85 -> setText(R.string.wmo_85)
        86 -> setText(R.string.wmo_86)

        95 -> setText(R.string.wmo_95)

        96 -> setText(R.string.wmo_96)
        99 -> setText(R.string.wmo_99)
    }
}

object DailyToUiConverter {
    fun convert(dailyList: List<Daily>) = dailyList.map {
        convertDaily(it)
    }
}

private fun convertDaily(daily: Daily) = DailyUi(
    daily.hourlyWeather,
    daily.apparentTemperatureMax,
    daily.apparentTemperatureMin,
    daily.precipitationHours,
    daily.precipitationSum,
    daily.sunrise,
    daily.sunset,
    daily.temperatureMax,
    daily.temperatureMin,
    daily.time,
    daily.weatherCode,
    daily.windSpeedMax,
    isExpanded = false
)
