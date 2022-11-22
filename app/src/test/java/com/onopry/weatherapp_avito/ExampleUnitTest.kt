package com.onopry.weatherapp_avito

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

data class City(
    val name: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val state: String
)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    fun initCities() = listOf(
        City("Mosow", "RU", 1.1f, 1.2f, "Moscow oblast'"),
        City("Mosow", "RU", 1.3f, 1.4f, "Moscow oblast'"),
        City("Novosibirsk", "RU", 2.1f, 4.2f, "Novosibirskaya oblast'"),
        City("Novosibirsk", "RU", 2.2f, 4.3f, "Novosibirskaya oblast'"),
        City("Piter", "RU", 5.1f, 6.2f, "SPB oblast'"),
    )

    @Test
    fun myTest(){
        val list = initCities()
        val dList = list.distinctBy { Pair(it.country, it.name) }
        println(list)
        println(dList)
    }
}