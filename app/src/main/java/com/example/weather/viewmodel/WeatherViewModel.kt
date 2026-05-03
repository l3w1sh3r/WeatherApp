package com.example.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.WeatherResponse
import com.example.weather.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state

    fun fetchWeather(city: String) {
        _state.value = WeatherState.Loading

        viewModelScope.launch {
            try {
                val data = repo.getWeather(city)
                _state.value = WeatherState.Success(data)
            } catch (e: Exception) {
                _state.value = WeatherState.Error("Không lấy được dữ liệu")
            }
        }
    }
}

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}