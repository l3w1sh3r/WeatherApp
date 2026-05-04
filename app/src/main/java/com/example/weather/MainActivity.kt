package com.example.weather

import WeatherApi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.ui.screen.DetailScreen
import com.example.weather.ui.screen.HomeScreen
import com.example.weather.ui.screen.SearchScreen
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
            val navController = rememberNavController()

            NavHost(navController, startDestination = "home") {

                composable("home") {
                    HomeScreen(viewModel, navController)
                }

                composable(
                    "detail/{city}/{temp}/{desc}"
                ) { backStackEntry ->

                    val city = backStackEntry.arguments?.getString("city") ?: ""
                    val temp = backStackEntry.arguments?.getString("temp") ?: ""
                    val desc = backStackEntry.arguments?.getString("desc") ?: ""

                    DetailScreen(city, temp, desc, navController)
                }

                composable("search") {
                    SearchScreen(navController, viewModel)
                }
            }
        }
    }
}