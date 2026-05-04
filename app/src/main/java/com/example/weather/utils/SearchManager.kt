package com.example.weather.utils

import android.content.Context
import androidx.core.content.edit

object RecentSearchManager {

    private const val PREF_NAME = "weather_prefs"
    private const val KEY_RECENT = "recent_searches"

    fun saveSearch(context: Context, city: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = getSearches(context).toMutableList()

        // tránh trùng
        current.remove(city)
        current.add(0, city)

        // giới hạn 5 item
        val updated = current.take(5)

        prefs.edit { putStringSet(KEY_RECENT, updated.toSet()) }
    }

    fun getSearches(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_RECENT, emptySet())?.toList() ?: emptyList()
    }
}