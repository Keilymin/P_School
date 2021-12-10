package com.burlakov.week1application.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar

class BatteryReceiver(var baseView : View) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent!!.getIntExtra("level", 0)
        Snackbar.make(baseView, "$level%", Snackbar.LENGTH_LONG)
            .show()
    }
}