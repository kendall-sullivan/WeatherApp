package com.ait.aitweatherapp.di

import com.ait.aitweatherapp.network.WeatherAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @Singleton
    fun provideWeatherRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherAPI(retrofit: Retrofit): WeatherAPI {
        return retrofit.create(WeatherAPI::class.java)
    }
}


