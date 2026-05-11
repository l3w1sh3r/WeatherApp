package com.example.weather.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.data.model.ForecastItem
import com.example.weather.viewmodel.WeatherState
import com.example.weather.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ForecastScreen(city: String, navController: NavController, viewModel: WeatherViewModel) {
    val state = viewModel.state.collectAsState().value

    // LẤY THÔNG TIN HIỆN TẠI ĐỂ LÀM NỀN
    // Thay vì lấy từ forecast.list, ta lấy từ state.weather (dữ liệu hiện tại)
    val currentDesc = if (state is WeatherState.Success) {
        // state.weather là thông tin thời tiết hiện tại giống hệt màn hình Detail
        state.weather.weather[0].description
    } else ""

    Box(modifier = Modifier.fillMaxSize()) {
        // Ảnh nền sẽ được cập nhật theo currentDesc (thời tiết hiện tại)
        Image(
            painter = painterResource(id = getBackgroundImage(currentDesc)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 3. NỘI DUNG CHÍNH
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text(Uri.decode(city), fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Color.White)
            }

            Text("Dự báo 5 ngày tới", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(vertical = 16.dp))

            if (state is WeatherState.Success) {
                val dailyData = state.forecast.list
                    .groupBy { it.dtTxt.substring(0, 10) }
                    .map { (_, itemsInDay) ->
                        val realMin = itemsInDay.minOf { it.main.temp_min }
                        val realMax = itemsInDay.maxOf { it.main.temp_max }
                        val hasRain = itemsInDay.any { it.weather[0].description.lowercase().contains("mưa") }
                        val hasCloud = itemsInDay.any { it.weather[0].description.lowercase().contains("mây") }

                        val bestDescription = when {
                            hasRain -> "mưa"
                            hasCloud -> "mây"
                            else -> "nắng"
                        }

                        val representative = itemsInDay.find { it.dtTxt.contains("12:00:00") } ?: itemsInDay.first()
                        representative.copy(
                            main = representative.main.copy(temp_min = realMin, temp_max = realMax),
                            weather = listOf(representative.weather[0].copy(description = bestDescription))
                        )
                    }.take(5)

                // Card cũng làm hiệu ứng kính mờ
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                        itemsIndexed(dailyData) { index, item ->
                            ForecastRow(item)
                            if (index < dailyData.size - 1) {
                                HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastRow(item: ForecastItem) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = sdf.parse(item.dtTxt)
    val dayName = SimpleDateFormat("EEEE", Locale("vi", "VN")).format(date ?: Date()).replaceFirstChar { it.uppercase() }

    val desc = item.weather[0].description.lowercase()
    val weatherIcon = when {
        desc.contains("mưa") -> "🌧️"
        desc.contains("mây") -> "⛅"
        else -> "☀️"
    }

    val minTemp = item.main.temp_min.toInt()
    val maxTemp = item.main.temp_max.toInt()

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(dayName, modifier = Modifier.weight(1.2f), fontSize = 17.sp, color = Color.White)

        Text(weatherIcon, modifier = Modifier.weight(0.8f), fontSize = 22.sp, textAlign = TextAlign.Center)

        Text("$minTemp°", modifier = Modifier.weight(0.6f), textAlign = TextAlign.End, fontSize = 17.sp, color = Color.White.copy(alpha = 0.7f))

        Box(
            modifier = Modifier
                .weight(1.5f)
                .padding(horizontal = 8.dp)
                .height(5.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(Color(0xFF5AC8FA), Color(0xFFFF9500))
                    ),
                    shape = RoundedCornerShape(3.dp)
                )
        )

        Text("$maxTemp°", modifier = Modifier.weight(0.6f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.White)
    }
}