package com.example.weather

import WeatherApi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.ui.screen.HomeScreen
import com.example.weather.viewmodel.WeatherViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherApi::class.java)
        val repo = WeatherRepository(api)
        val viewModel = WeatherViewModel(repo)

        setContent {
            HomeScreen(viewModel)
        }
    }
}