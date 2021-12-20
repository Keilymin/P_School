package com.burlakov.week1application.util

import android.content.Context
import android.content.SharedPreferences

class NotificationSettingsUtil(context: Context) {
    val name = "Notifications"
    val mode = Context.MODE_PRIVATE

    private var switch = "Switch"
    var switchIsChecked: Boolean = false

    private var editText = "EditText"
    var searchText: String? = null

    private var spinner = "Spinner"
    var spinnerPos: Int = 0

    val prefs: SharedPreferences = context.getSharedPreferences(ThemeUtil.name, ThemeUtil.mode)

    init {
        load()
    }

    fun load(){
        switchIsChecked = prefs.getBoolean(switch, false)
        searchText = prefs.getString(editText, "")
        spinnerPos = prefs.getInt(spinner, 0)
    }

    fun save(switchIsChecked: Boolean, searchText: String, spinnerPos: Int) {
        val edit = prefs.edit()
        edit.putBoolean(switch, switchIsChecked)
        edit.putString(editText, searchText)
        edit.putInt(spinner, spinnerPos)
        edit.apply()
    }


}