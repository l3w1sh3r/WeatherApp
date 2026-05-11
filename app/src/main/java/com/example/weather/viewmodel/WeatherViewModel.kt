package com.example.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.WeatherResponse
import com.example.weather.data.model.ForecastResponse
import com.example.weather.data.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state

    fun fetchWeather(city: String) {
        // Cực kỳ quan trọng: In ra Logcat để biết app có nhận được chữ bạn gõ không
        println("LOG_WEATHER: Đang tìm kiếm thành phố -> [$city]")
        _state.value = WeatherState.Loading

        viewModelScope.launch {
            try {
                // 1. Thử lấy thời tiết hiện tại trước (Thằng này quan trọng nhất)
                val weatherData = repo.getWeather(city)
                println("LOG_WEATHER: Đã lấy xong thời tiết hiện tại!")

                // 2. Nếu lấy thời tiết thành công, mới bắt đầu thử lấy dự báo
                try {
                    val forecastData = repo.getForecast(city)
                    println("LOG_WEATHER: Đã lấy xong dự báo 5 ngày!")

                    // Thành công mỹ mãn cả 2 API
                    _state.value = WeatherState.Success(weatherData, forecastData)

                } catch (e: Exception) {
                    // Nếu lỗi ở dự báo (Ví dụ: API key chưa hỗ trợ)
                    println("LOG_ERROR: Lỗi API Dự báo -> ${e.message}")
                    _state.value = WeatherState.Error("Thành phố đúng, nhưng lỗi tải Dự báo: ${e.message}")
                }

            } catch (e: Exception) {
                // Nếu lỗi ngay từ lúc lấy thời tiết (Do sai tên, gõ thừa dấu cách...)
                println("LOG_ERROR: Lỗi API Thời tiết -> ${e.message}")
                _state.value = WeatherState.Error("Không tìm thấy thành phố, vui lòng thử lại!")
            }
        }
    }
}

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    // Thành công phải chứa cả 2 loại dữ liệu này
    data class Success(val weather: WeatherResponse, val forecast: ForecastResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}