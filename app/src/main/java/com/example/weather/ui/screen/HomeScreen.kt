package com.example.weather.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather.viewmodel.WeatherState
import com.example.weather.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(viewModel: WeatherViewModel) {
    val state = viewModel.state.collectAsState().value
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Nhập thành phố") }
        )

        Button(onClick = { viewModel.fetchWeather(city) }) {
            Text("Xem thời tiết")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is WeatherState.Loading -> {
                CircularProgressIndicator()
            }

            is WeatherState.Success -> {
                Text("Thành phố: ${state.data.name}")
                Text("Nhiệt độ: ${state.data.main.temp}°C")
                Text("Trạng thái: ${state.data.weather[0].description}")
            }

            is WeatherState.Error -> {
                Text(state.message)
            }

            else -> {}
        }
    }
}