package com.burlakov.week1application.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.burlakov.week1application.R
import com.burlakov.week1application.viewmodels.LogInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogInActivity : AppCompatActivity() {

    lateinit var singInButton: Button
    lateinit var usernameEditText: EditText
    private val logInViewModel: LogInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        


        supportActionBar?.title = getString(R.string.login)

        singInButton = findViewById(R.id.singIn)
        usernameEditText = findViewById(R.id.username)

        logInViewModel.logInResult.observe(this, {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            }
        })

        singInButton.setOnClickListener {
            if (usernameEditText.text.toString().trim().isNotEmpty()) {
                logInViewModel.singIn(usernameEditText.text.toString())
            }
        }
    }
}
