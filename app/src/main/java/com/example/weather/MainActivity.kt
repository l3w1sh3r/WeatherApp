package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weather.data.api.WeatherApi
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.ui.screen.HomeScreen
import com.example.weather.ui.screen.SearchScreen
import com.example.weather.ui.screen.DetailScreen
import com.example.weather.ui.screen.ForecastScreen
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
        val weatherViewModel = WeatherViewModel(repo)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {

                // 1. Màn hình chính
                composable("home") {
                    HomeScreen(viewModel = weatherViewModel, navController = navController)
                }

                // 2. Màn hình tìm kiếm
                composable("search") {
                    SearchScreen(navController = navController, viewModel = weatherViewModel)
                }

                // 3. Màn hình chi tiết (Truyền 5 tham số để hiển thị dữ liệu thật)
                composable(
                    route = "detail/{city}/{temp}/{desc}/{humidity}/{wind}",
                    arguments = listOf(
                        navArgument("city") { type = NavType.StringType },
                        navArgument("temp") { type = NavType.StringType },
                        navArgument("desc") { type = NavType.StringType },
                        navArgument("humidity") { type = NavType.StringType },
                        navArgument("wind") { type = NavType.StringType }
                    )
                ) { entry ->
                    DetailScreen(
                        city = entry.arguments?.getString("city") ?: "",
                        temp = entry.arguments?.getString("temp") ?: "",
                        desc = entry.arguments?.getString("desc") ?: "",
                        humidity = entry.arguments?.getString("humidity") ?: "",
                        wind = entry.arguments?.getString("wind") ?: "",
                        navController = navController
                    )
                }

                // 4. Màn hình dự báo 5 ngày
                composable(
                    route = "forecast/{city}",
                    arguments = listOf(navArgument("city") { type = NavType.StringType })
                ) { entry ->
                    ForecastScreen(
                        city = entry.arguments?.getString("city") ?: "",
                        navController = navController,
                        viewModel = weatherViewModel
                    )
                }
            }
        }
    }
}