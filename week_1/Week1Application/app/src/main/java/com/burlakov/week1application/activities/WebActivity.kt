package com.burlakov.week1application.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R


class WebActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_URL = BuildConfig.APPLICATION_ID + ".extra.PHOTO_URL"
    }

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView = findViewById(R.id.webView)

        webView.loadUrl(intent.getStringExtra(PHOTO_URL)!!)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}