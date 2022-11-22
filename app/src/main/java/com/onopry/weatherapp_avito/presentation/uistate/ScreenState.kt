package com.onopry.weatherapp_avito.presentation.uistate

import com.onopry.domain.model.forecast.Forecast
import com.onopry.domain.model.forecast.Locality

sealed class ScreenState {
    object Empty: ScreenState()
    class LocationError(val msg: String): ScreenState()
    class ForecastError(val msg: String): ScreenState()
    class Content(val forecast: Forecast): ScreenState()
    object Loading: ScreenState()
}

