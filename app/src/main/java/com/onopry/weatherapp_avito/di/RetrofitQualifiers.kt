package com.onopry.weatherapp_avito.di

import javax.inject.Qualifier

object RetrofitQualifiers {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Location

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Forecast

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IpGeolocation
}