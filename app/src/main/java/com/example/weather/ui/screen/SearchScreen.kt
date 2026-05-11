package com.example.weather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weather.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current // Công cụ để ẩn bàn phím

    // Danh sách lịch sử tìm kiếm giả lập
    val searchHistory = listOf("Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ", "London", "Paris", "Tokyo")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // 1. Thanh tìm kiếm kèm nút Hủy (iOS Style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm kiếm thành phố", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                },
                singleLine = true,

                // --- XỬ LÝ NÚT ENTER TRÊN BÀN PHÍM ---
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search // Biến nút Enter thành nút Tìm kiếm
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.fetchWeather(searchQuery) // Gọi API
                            focusManager.clearFocus()            // Ẩn bàn phím
                            navController.popBackStack()         // Quay về Home
                        }
                    }
                ),
                // --------------------------------------

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F7),
                    unfocusedContainerColor = Color(0xFFF2F2F7),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF007AFF)
                )
            )

            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    navController.popBackStack()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Hủy", color = Color(0xFF007AFF), fontSize = 17.sp)
            }
        }

        // 2. Tiêu đề Lịch sử
        Text(
            text = "LỊCH SỬ TÌM KIẾM",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 3. Danh sách lịch sử
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchHistory) { city ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            focusManager.clearFocus()
                            viewModel.fetchWeather(city)
                            navController.popBackStack()
                        }
                ) {
                    Text(
                        text = city,
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}