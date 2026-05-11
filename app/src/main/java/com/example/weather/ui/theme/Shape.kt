package com.example.weather.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val IosShapes = Shapes(
    small = RoundedCornerShape(8.dp),  // Dùng cho nút bấm nhỏ, text field
    medium = RoundedCornerShape(14.dp), // Dùng cho hình ảnh
    large = RoundedCornerShape(20.dp)  // CHÍNH LÀ ĐÂY: Dùng cho các Card thời tiết
)