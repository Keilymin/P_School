package com.burlakov.week1application.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {

    companion object {
        fun isOnNetwork(context: Context): Boolean {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected == true
        }
    }
}