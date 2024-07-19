package ru.ndevelop.yandexhomework.presentation.screens

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

object ThemeHelper {
    const val LIGHT_MODE = "light"
    const val DARK_MODE = "dark"
    const val SYSTEM_DEFAULT = "system"

    fun applyTheme(themePref: String) {
        when (themePref) {
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SYSTEM_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun applyThemeFromPreferences(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val themePref = sharedPreferences.getString("theme_preference", SYSTEM_DEFAULT)
        applyTheme(themePref ?: SYSTEM_DEFAULT)
    }
}