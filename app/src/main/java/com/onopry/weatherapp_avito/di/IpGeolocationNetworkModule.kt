package com.onopry.weatherapp_avito.di

import com.onopry.data.datasources.remote.iplocation.IpGeolocationApi
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IpGeolocationNetworkModule {
    @Provides
    @Singleton
    @RetrofitQualifiers.IpGeolocation
    fun provideIpGeolocationOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addQueryParam("apiKey", ApiKeys.IP_GEOLOCATION_KEY)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RetrofitQualifiers.IpGeolocation
    fun provideGeoLocationRetrofit(moshi: Moshi, @RetrofitQualifiers.IpGeolocation client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.ipgeolocation.io/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideGeoLocationApi(@RetrofitQualifiers.IpGeolocation retrofit: Retrofit): IpGeolocationApi = retrofit.create(
        IpGeolocationApi::class.java)
}