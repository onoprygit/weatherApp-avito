package com.onopry.weatherapp_avito.presentation.uistate

sealed class PermissionState {
    object Empty : PermissionState()
    object Required : PermissionState()
    object Pending : PermissionState()
    object Denied : PermissionState()
    object Granted : PermissionState()
}