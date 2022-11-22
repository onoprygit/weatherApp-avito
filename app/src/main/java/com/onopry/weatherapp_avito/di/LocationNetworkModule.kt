package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.location.LocationApi
import com.onopry.weatherapp_avito.utils.ApiKeys
import com.onopry.weatherapp_avito.utils.addQueryParam
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.time.Duration

@Module
@InstallIn(SingletonComponent::class)
object LocationNetworkModule {
    @Provides
    @Singleton
    @RetrofitQualifiers.Location
    fun provideLocationOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addQueryParam("appid", ApiKeys.LOCATION_KEY)
//        .connectTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RetrofitQualifiers.Location
    fun provideLocationRetrofit(moshi: Moshi, @RetrofitQualifiers.Location client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/geo/1.0/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideLocationApi(@RetrofitQualifiers.Location retrofit: Retrofit): LocationApi = retrofit.create(
        LocationApi::class.java)
}