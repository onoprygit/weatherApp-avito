package com.onopry.weatherapp_avito.presentation.uistate

sealed class ScreenState {
    object Empty: ScreenState()
    class LocationError(val msg: String): ScreenState()
    class ForecastError(val msg: String): ScreenState()
}