package com.example.weather.data.api

import com.example.weather.data.model.ForecastResponse
import com.example.weather.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApi {


    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecastByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): ForecastResponse

}