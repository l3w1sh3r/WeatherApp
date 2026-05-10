package com.example.weather.utils

import android.content.Context
import androidx.core.content.edit

object RecentSearchManager {

    private const val PREF_NAME = "weather_prefs"
    private const val KEY_RECENT = "recent_searches"

    fun saveSearch(context: Context, city: String) {

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val current = getSearches(context).toMutableList()

        // xoá nếu đã tồn tại
        current.remove(city)

        // thêm lên đầu
        current.add(0, city)

        // giới hạn 5 item
        val updated = current.take(5)

        // lưu thành chuỗi
        val result = updated.joinToString(",")

        prefs.edit {
            putString(KEY_RECENT, result)
        }
    }

    fun getSearches(context: Context): List<String> {

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val data = prefs.getString(KEY_RECENT, "") ?: ""

        if (data.isEmpty()) return emptyList()

        return data.split(",")
    }
}