package com.onopry.weatherapp_avito.presentation.states

sealed class LocationState {
    object Empty: LocationState()
    object Pending : LocationState()
    object Denied : LocationState()
    class Granted(
        val latitude: Float,
        val longitude: Float,
        val isNative: Boolean = true
    ) : LocationState()
}
