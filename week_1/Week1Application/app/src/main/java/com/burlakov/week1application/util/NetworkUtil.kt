package com.burlakov.week1application.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.burlakov.week1application.R

@Suppress("DEPRECATION")
class NetworkUtil {

    companion object {
        fun isOnNetwork(context: Context): Boolean {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected == true
        }

        fun checkConnection(context: Context): Boolean {
            return if (isOnNetwork(context)) {
                true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.internet_not_connected),
                    Toast.LENGTH_LONG
                )
                    .show()
                false
            }
        }
    }
}