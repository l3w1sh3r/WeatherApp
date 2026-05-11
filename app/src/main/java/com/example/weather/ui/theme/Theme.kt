package com.example.weather.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Áp dụng bảng màu iOS cho chế độ sáng
private val LightColorScheme = lightColorScheme(
    primary = IosSystemBlue,       // Đổi màu mặc định của Button thành xanh iOS
    background = IosBackgroundLight, // Đổi màu nền app thành xám iOS
    surface = IosSurfaceLight,       // Đổi màu nền Card thành trắng
    onPrimary = IosSurfaceLight,     // Chữ trên nút màu xanh sẽ là màu trắng
    onBackground = IosTextPrimary,   // Chữ trên nền app là màu đen
    onSurface = IosTextPrimary       // Chữ trên Card là màu đen
)

// (Bạn có thể cấu hình tương tự cho DarkColorScheme nếu app có Dark Mode)

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Tạm thời ép dùng Light Mode để giống iOS nhất

    // 2. Chỉnh thanh trạng thái (cục pin, wifi) trên cùng màn hình hòa vào nền
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // 3. Khởi chạy MaterialTheme với các cấu hình mới
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = IosShapes, // Đưa Shape bo tròn 20.dp vào đây
        typography = Typography,
        content = content
    )
}