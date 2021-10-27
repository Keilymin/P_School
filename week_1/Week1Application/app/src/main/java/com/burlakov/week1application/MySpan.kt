package com.burlakov.week1application

import android.content.Intent
import android.text.style.URLSpan
import android.view.View
import com.burlakov.week1application.activities.WebActivity

class MySpan(url: String?) : URLSpan(url) {
    override fun onClick(widget: View) {
        val intent = Intent(widget.context, WebActivity::class.java)
        intent.putExtra(WebActivity.PHOTO_URL, url)
        widget.context.startActivity(intent)
    }
}