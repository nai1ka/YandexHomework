package ru.ndevelop.yandexhomework.presentation.screens.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.presentation.screens.ThemeHelper

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)
        val themePreference: ListPreference? = findPreference("theme_preference")
        themePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                ThemeHelper.applyTheme(newValue as String)
                true
            }
    }
}