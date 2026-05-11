package com.example.weather.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.R // CHÚ Ý: Kiểm tra lại import này cho khớp với package của bạn
import com.example.weather.viewmodel.WeatherState
import com.example.weather.viewmodel.WeatherViewModel



@Composable
fun HomeScreen(viewModel: WeatherViewModel, navController: NavController) {
    val state = viewModel.state.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F7)).padding(horizontal = 20.dp)) {
        Text(
            text = "Thời tiết",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth().height(44.dp).clickable { navController.navigate("search") },
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFE3E3E8)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Tìm kiếm thành phố", color = Color.Gray, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (state) {
                is WeatherState.Success -> {
                    val w = state.weather
                    val desc = w.weather[0].description

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp) // THÊM: Cố định chiều cao để ảnh nền có không gian hiển thị đẹp
                            .clickable {
                                val city = Uri.encode(w.name)
                                val descEnc = Uri.encode(desc)
                                navController.navigate("detail/$city/${w.main.temp}/$descEnc/${w.main.humidity}/${w.wind.speed}")
                            },
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // THÊM: Đổ bóng nhẹ cho Card nổi lên
                    ) {
                        // SỬ DỤNG BOX ĐỂ XẾP CHỒNG: 1. Ảnh -> 2. Màng đen -> 3. Chữ
                        Box(modifier = Modifier.fillMaxSize()) {

                            // LỚP 1: ẢNH NỀN THỜI TIẾT
                            Image(
                                painter = painterResource(id = getBackgroundImage(desc)),
                                contentDescription = "Weather Background",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop // Cắt ảnh vừa khít Card
                            )

                            // LỚP 2: MÀNG KÍNH MỜ (Tối 30% để chữ trắng dễ đọc)
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

                            // LỚP 3: NỘI DUNG (GIỮ NGUYÊN LOGIC CHỐNG TRÀN CHỮ)
                            Row(
                                modifier = Modifier.padding(24.dp).fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = w.name,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White, // ĐỔI MÀU: Trắng
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = desc.replaceFirstChar { it.uppercase() },
                                        color = Color.White.copy(alpha = 0.8f), // ĐỔI MÀU: Trắng mờ
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    text = "${w.main.temp.toInt()}°",
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White, // ĐỔI MÀU: Trắng
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                is WeatherState.Loading -> CircularProgressIndicator(color = Color(0xFF007AFF))
                is WeatherState.Error -> Text(state.message, color = Color.Red)
                else -> Text("Nhập thành phố để bắt đầu", color = Color.Gray)
            }
        }
    }
}