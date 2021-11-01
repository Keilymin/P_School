package com.burlakov.week1application.util

import android.content.Intent
import android.text.style.URLSpan
import android.view.View
import android.widget.Toast
import com.burlakov.week1application.activities.WebActivity

class MySpan(url: String?) : URLSpan(url) {
    override fun onClick(widget: View) {
        if (url != null) {
            val intent = Intent(widget.context, WebActivity::class.java)
            intent.putExtra(WebActivity.PHOTO_URL, url)
            widget.context.startActivity(intent)
        } else Toast.makeText(widget.context, "Url is null", Toast.LENGTH_SHORT).show()
    }
}