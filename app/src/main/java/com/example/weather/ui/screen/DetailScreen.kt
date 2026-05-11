package com.example.weather.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.R // CHÚ Ý: Đảm bảo dòng này khớp với tên package của bạn

// 1. HÀM TỰ ĐỘNG CHỌN ẢNH THEO THỜI TIẾT
fun getBackgroundImage(desc: String): Int {
    val lowerDesc = desc.lowercase()
    return when {
        lowerDesc.contains("mưa") -> R.drawable.bg_rainy
        lowerDesc.contains("mây") -> R.drawable.bg_cloudy
        else -> R.drawable.bg_sunny
    }
}

// 2. GIAO DIỆN CHÍNH
@Composable
fun DetailScreen(city: String, temp: String, desc: String, humidity: String, wind: String, navController: NavController) {
    val decodedCity = Uri.decode(city)
    val decodedDesc = Uri.decode(desc)

    // BOX to nhất: Đóng vai trò như một cái khung để xếp chồng các lớp lên nhau
    Box(modifier = Modifier.fillMaxSize()) {

        // --- LỚP DƯỚI CÙNG: ẢNH NỀN ---
        Image(
            painter = painterResource(id = getBackgroundImage(decodedDesc)),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Lệnh này giúp ảnh tự động zoom cho kín màn hình
        )

        // --- LỚP GIỮA: MÀNG KÍNH MỜ ---
        // (Phủ một lớp đen mờ 30% lên ảnh để chữ màu trắng bên trên không bị chìm)
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        // --- LỚP TRÊN CÙNG: NỘI DUNG THỜI TIẾT ---
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    Text(" Quay lại", color = Color.White, fontSize = 17.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(decodedCity, fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("${temp.toDouble().toInt()}°", fontSize = 96.sp, fontWeight = FontWeight.Light, color = Color.White)
            Text(decodedDesc.replaceFirstChar { it.uppercase() }, color = Color.White, fontSize = 20.sp)

            // Thẻ Card cũng được làm hiệu ứng kính trong suốt (alpha = 0.2f)
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    DetailRow("Độ ẩm", "$humidity%")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.White.copy(alpha = 0.5f))
                    DetailRow("Sức gió", "$wind m/s")
                }
            }

            Button(
                onClick = { navController.navigate("forecast/${Uri.encode(decodedCity)}") },
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Xem dự báo 5 ngày tới", fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 17.sp, color = Color.White)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.White)
    }
}