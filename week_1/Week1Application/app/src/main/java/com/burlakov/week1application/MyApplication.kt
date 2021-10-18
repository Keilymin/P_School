package com.burlakov.week1application

import android.app.Application
import android.content.Context


class MyApplication : Application() {

    val COUNTER = "Counter"
    val count = "count"

    companion object {
        var value: Int = 0
    }

    override fun onCreate() {
        super.onCreate()
        val sp = getSharedPreferences(
            COUNTER,
            Context.MODE_PRIVATE
        )
        value = sp.getInt(count, 0) + 1

        val prefs = getSharedPreferences(COUNTER, MODE_PRIVATE)
        val prefEdit = prefs.edit()
        prefEdit.putInt("count", value)
        prefEdit.apply()
    }
}