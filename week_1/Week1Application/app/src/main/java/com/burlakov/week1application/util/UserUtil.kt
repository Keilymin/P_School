package com.burlakov.week1application.util

import android.content.Context
import com.burlakov.week1application.models.User

class UserUtil {
    companion object {

        val name = "Theme"
        val mode = Context.MODE_PRIVATE
        val userId = "UserId"
        val username = "UserName"

        fun getUser(context: Context): User? {
            val prefs = context.getSharedPreferences(name, mode)
            val usnm = prefs.getString(username, null)
            val usId = prefs.getLong(userId, -1)
            return if (usnm != null && usId != -1L) {
                val user = User(usnm)
                user.userId = usId
                user
            } else null
        }

        fun saveUser(context: Context, user: User) {
            if (user.userId != null) {
                val prefs = context.getSharedPreferences(name, mode)
                val edit = prefs.edit()
                edit.putString(username, user.username)
                edit.putLong(userId, user.userId!!)
                edit.apply()
            }
        }
        fun logOut(context: Context){
            val prefs = context.getSharedPreferences(name, mode)
            val edit = prefs.edit()
            edit.clear().apply()
        }
    }
}
