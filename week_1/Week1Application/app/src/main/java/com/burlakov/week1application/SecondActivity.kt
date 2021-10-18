package com.burlakov.week1application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity(), View.OnClickListener {



    companion object {
        val EXTRA_CHOICE = "com.burlakov.week1application.extra.CHOICE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val choice1 : Button = findViewById(R.id.choice1)
        choice1.setOnClickListener(this)

        val choice2 : Button = findViewById(R.id.choice2)
        choice2.setOnClickListener(this)

        val choice3 : Button = findViewById(R.id.choice3)
        choice3.setOnClickListener(this)

        val choice4 : Button = findViewById(R.id.choice4)
        choice4.setOnClickListener(this)

        val choice5 : Button = findViewById(R.id.choice5)
        choice5.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v is Button) {
            val choiceIntent = Intent()
            choiceIntent.putExtra(EXTRA_CHOICE, v.text.toString())
            setResult(RESULT_OK, choiceIntent)
            finish()
        }
    }
}