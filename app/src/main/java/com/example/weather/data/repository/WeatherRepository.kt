package com.example.weather.data.repository

import com.example.weather.data.api.WeatherApi
import com.example.weather.data.model.WeatherResponse

class WeatherRepository(private val api: WeatherApi) {
    private val apiKey = "09ce14f8551349b7256e029552b64c34"

    suspend fun getWeather(city: String) = api.getWeatherByCity(city, apiKey)

    suspend fun getForecast(city: String) = api.getForecastByCity(city, apiKey)
}