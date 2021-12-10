package com.burlakov.week1application.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.burlakov.week1application.R
import com.burlakov.week1application.util.ThemeUtil

class SettingsFragment : Fragment(), View.OnClickListener {

    lateinit var group: RadioGroup
    lateinit var light: RadioButton
    lateinit var dark: RadioButton
    lateinit var auto: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        group = root.findViewById(R.id.radioGroup)
        light = root.findViewById(R.id.light)
        dark = root.findViewById(R.id.dark)
        auto = root.findViewById(R.id.auto)


        light.setOnClickListener(this)
        dark.setOnClickListener(this)
        auto.setOnClickListener(this)

        when (ThemeUtil.getTheme(requireContext())) {
            ThemeUtil.AUTO -> auto.isChecked = true
            ThemeUtil.DAY -> light.isChecked = true
            ThemeUtil.NIGHT -> dark.isChecked = true
        }

        return root
    }

    override fun onClick(v: View?) {
        if (v is RadioButton) {
            val checked = v.isChecked

            when (v.getId()) {
                R.id.dark ->
                    if (checked) {
                        ThemeUtil.setTheme(requireContext(), ThemeUtil.NIGHT)
                    }
                R.id.light ->
                    if (checked) {
                        ThemeUtil.setTheme(requireContext(), ThemeUtil.DAY)
                    }
                R.id.auto ->
                    if (checked) {
                        ThemeUtil.setTheme(requireContext(), ThemeUtil.AUTO)
                    }
            }
        }
    }

}