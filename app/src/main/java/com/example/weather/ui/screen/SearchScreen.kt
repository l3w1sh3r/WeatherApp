package com.example.weather.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather.utils.RecentSearchManager
import com.example.weather.viewmodel.WeatherViewModel


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var recent by remember { mutableStateOf(RecentSearchManager.getSearches(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.popBackStack() }
        ) {
            Text("←")
        }

        TextField(
            value = query,
            onValueChange = { newText: String ->
                query = newText
            },
            label = { Text("Nhập thành phố") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (query.isNotBlank()) {
                viewModel.fetchWeather(query)
                RecentSearchManager.saveSearch(context, query)
                recent = RecentSearchManager.getSearches(context)

                navController.popBackStack()
            }
        }) {
            Text("Tìm kiếm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tìm kiếm gần đây")

        recent.forEach { city ->
            Text(
                text = city,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.fetchWeather(city)
                        navController.popBackStack()
                    }
                    .padding(8.dp)
            )
        }
    }
}