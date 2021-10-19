package com.burlakov.week1application

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {


    private lateinit var sharedText: TextView
    private lateinit var textFromSecondActivity: TextView
    private lateinit var counter: TextView
    private lateinit var choose: Button
    private lateinit var share: Button

    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                val choice = result.data?.getStringExtra(SecondActivity.EXTRA_CHOICE)
                textFromSecondActivity.text = choice
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedText = findViewById(R.id.sharedText)
        textFromSecondActivity = findViewById(R.id.textFromSecondActivity)
        choose = findViewById(R.id.choose)
        share = findViewById(R.id.share)
        counter = findViewById(R.id.counter)

        val data: Uri? = intent?.data

        if (intent?.type == "text/plain") {
            sharedText.isVisible = true
            sharedText.text = intent?.getStringExtra(Intent.EXTRA_TEXT)
        }

        choose.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            getContent.launch(intent)
        }

        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textFromSecondActivity.text.toString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        counter.text = MyApplication.value.toString()

    }
}