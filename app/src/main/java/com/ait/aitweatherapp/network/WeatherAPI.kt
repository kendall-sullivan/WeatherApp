package com.ait.aitweatherapp.network

import com.ait.aitweatherapp.data.weather.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=e5f13ed33afee3f236b10358f5444d25
// Host: https://api.openweathermap.org/
// Path: /data/2.5/weather
// Query params: ?q=Budapest,hu&units=metric&appid=e5f13ed33afee3f236b10358f5444d25

interface WeatherAPI {

    @GET("/data/2.5/weather")
    suspend fun getWeatherData(@Query("q") cityName: String,
                               @Query("units") units: String,
                               @Query("appid") appId: String): WeatherResult
}
