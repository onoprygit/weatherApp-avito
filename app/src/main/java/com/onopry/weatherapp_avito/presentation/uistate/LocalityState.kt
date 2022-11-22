package com.onopry.weatherapp_avito.presentation.uistate

import com.onopry.domain.model.forecast.Locality

sealed class LocalityState {
    object Empty : LocalityState()
    object None : LocalityState()
    object Pending : LocalityState()
    class Error(val msg: String) : LocalityState()
    class City(
        val locality: Locality,
        val isIpLocality: Boolean
    ) : LocalityState()
}
