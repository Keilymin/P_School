package com.burlakov.week1application.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class ThemeUtil {

    companion object {
        const val AUTO = 0
        const val DAY = 1
        const val NIGHT = 2

        val themeMode = "DayNight"
        val name = "Theme"
        val mode = Context.MODE_PRIVATE

        fun getTheme(context: Context): Int {
            val prefs = context.getSharedPreferences(name, mode)
            return prefs.getInt(themeMode, 0)
        }

        fun setTheme(context: Context) {
            changeTheme(getTheme(context))
        }

        fun setTheme(context: Context, theme: Int) {
            val prefs = context.getSharedPreferences(name, mode)
            val edit = prefs.edit()
            edit.putInt(themeMode, theme)
            edit.apply()
            changeTheme(theme)
        }

        private fun changeTheme(theme: Int) {
            when (theme) {
                AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}