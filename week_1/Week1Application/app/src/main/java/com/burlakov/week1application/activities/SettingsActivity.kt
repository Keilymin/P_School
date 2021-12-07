package com.burlakov.week1application.activities

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.burlakov.week1application.R
import com.burlakov.week1application.util.ThemeUtil

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var group: RadioGroup
    lateinit var light: RadioButton
    lateinit var dark: RadioButton
    lateinit var auto: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        group = findViewById(R.id.radioGroup)
        light = findViewById(R.id.light)
        dark = findViewById(R.id.dark)
        auto = findViewById(R.id.auto)


        light.setOnClickListener(this)
        dark.setOnClickListener(this)
        auto.setOnClickListener(this)

        when (ThemeUtil.getTheme(this)) {
            ThemeUtil.AUTO -> auto.isChecked = true
            ThemeUtil.DAY -> light.isChecked = true
            ThemeUtil.NIGHT -> dark.isChecked = true
        }

    }

    override fun onClick(v: View?) {
        if (v is RadioButton) {
            val checked = v.isChecked

            when (v.getId()) {
                R.id.dark ->
                    if (checked) {
                        ThemeUtil.setTheme(this, ThemeUtil.NIGHT)
                    }
                R.id.light ->
                    if (checked) {
                        ThemeUtil.setTheme(this, ThemeUtil.DAY)
                    }
                R.id.auto ->
                    if (checked) {
                        ThemeUtil.setTheme(this, ThemeUtil.AUTO)
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}