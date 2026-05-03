package com.example.weather.data.repository

import WeatherApi
import com.example.weather.data.model.WeatherResponse

class WeatherRepository(private val api: WeatherApi) {

    suspend fun getWeather(city: String): WeatherResponse {
        return api.getWeatherByCity(city, "09ce14f8551349b7256e029552b64c34")
    }
}