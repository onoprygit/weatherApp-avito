package com.onopry.domain.model.forecast

data class Hourly(
    val apparentTemperature: List<Int>,
    val freezingLevelHeight: List<Int>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Int>,
    val time: List<String>,
    val visibility: List<Int>,
    val weatherCode: List<Int>,
    val windDirection: List<Int>,
    val windSpeed: List<Int>
    /*  val snowDepth: List<Double>,*/
)

/*
data class ApparentTemperature(
    val apparentTemp: Int,
    val time: String
)
data class FreezingLevelHeight(
    val freezingLevelHeight: Int,
    val time: String
)

data class Precipitation(
    val precipitation: Int,
    val time: String
)

data class HourlyTest(
    val apparentTemperature: List<ApparentTemperature>,
    val freezingLevelHeight: List<FreezingLevelHeight>,
    val precipitation: List<Precipitation>,
    val rain: List<Double>,
    val relativeHumidity: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Int>,
    val time: List<String>,
    val visibility: List<Int>,
    val weatherCode: List<Int>,
    val windDirection: List<Int>,
    val windSpeed: List<Int>
  val snowDepth: List<Double>,

)

fun Hourly.mapto(){
    val size = time.size
    time.mapIndexed { index, time ->

    }
    val apparentTemperature = Array<ApparentTemperature>(size)
    val precipitationList = mutableListOf<Precipitation>()
    val freezingLevelHeightList = mutableListOf<FreezingLevelHeight>()
        for (i in 0..size){

        }

    return HourlyTest()
}
*/
