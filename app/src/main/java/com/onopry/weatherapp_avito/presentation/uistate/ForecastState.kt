package com.onopry.weatherapp_avito.presentation.uistate

import com.onopry.domain.model.forecast.Forecast

sealed class ForecastState{
    object Empty: ForecastState()
    object Loading: ForecastState()
    class Error(val message: String) : ForecastState()
    class Success(val data: Forecast): ForecastState()
}