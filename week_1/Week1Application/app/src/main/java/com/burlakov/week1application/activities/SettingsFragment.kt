package com.burlakov.week1application.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.work.*
import com.burlakov.week1application.R
import com.burlakov.week1application.notification.AutoSearchNotification
import com.burlakov.week1application.util.Constants
import com.burlakov.week1application.util.NotificationSettingsUtil
import com.burlakov.week1application.util.SearchWorker
import com.burlakov.week1application.util.ThemeUtil
import java.util.concurrent.TimeUnit


class SettingsFragment : Fragment(), View.OnClickListener {

    lateinit var group: RadioGroup
    lateinit var light: RadioButton
    lateinit var dark: RadioButton
    lateinit var auto: RadioButton

    lateinit var notificationSwitch: Switch
    lateinit var searchEditText: EditText
    lateinit var spinner: Spinner

    var blockCount = 0


    lateinit var notificationSettingsUtil: NotificationSettingsUtil

    private lateinit var notification: AutoSearchNotification

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        notification = AutoSearchNotification(requireContext())
        notificationSettingsUtil = NotificationSettingsUtil(requireContext())

        group = root.findViewById(R.id.radioGroup)
        light = root.findViewById(R.id.light)
        dark = root.findViewById(R.id.dark)
        auto = root.findViewById(R.id.auto)

        notificationSwitch = root.findViewById(R.id.notificationSwitch)
        searchEditText = root.findViewById(R.id.notificationEditText)
        spinner = root.findViewById(R.id.spinner)

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(), R.array.times_spinner,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        loadNotificationSettings()


        light.setOnClickListener(this)
        dark.setOnClickListener(this)
        auto.setOnClickListener(this)

        when (ThemeUtil.getTheme(requireContext())) {
            ThemeUtil.AUTO -> auto.isChecked = true
            ThemeUtil.DAY -> light.isChecked = true
            ThemeUtil.NIGHT -> dark.isChecked = true
        }

        notificationSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                spinner.visibility = View.VISIBLE
                searchEditText.visibility = View.VISIBLE
                if (searchEditText.text.toString() != notificationSettingsUtil.searchText) {
                    runWorker()
                }
            } else {
                spinner.visibility = View.GONE
                searchEditText.visibility = View.GONE
                WorkManager.getInstance(requireContext()).cancelAllWork()
            }
            saveNotificationSettings()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                if (selectedItemPosition != notificationSettingsUtil.spinnerPos) {
                    runWorker()
                    saveNotificationSettings()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        searchEditText.doOnTextChanged { _, _, _, _ ->
            if (searchEditText.text.toString() != notificationSettingsUtil.searchText) {
                runWorker()
                saveNotificationSettings()
            }
        }

        AutoSearchNotification.createChannel(requireActivity(), requireContext())


        return root
    }

    private fun loadNotificationSettings() {
        notificationSwitch.isChecked = notificationSettingsUtil.switchIsChecked
        searchEditText.setText(notificationSettingsUtil.searchText)
        spinner.setSelection(notificationSettingsUtil.spinnerPos)
        if (notificationSwitch.isChecked) {
            spinner.visibility = View.VISIBLE
            searchEditText.visibility = View.VISIBLE
        }
    }

    private fun saveNotificationSettings() {
        notificationSettingsUtil.save(
            notificationSwitch.isChecked,
            searchEditText.text.toString(),
            spinner.selectedItemPosition
        )
        notificationSettingsUtil.load()
    }

    private fun runWorker() {
        val searchText = searchEditText.text.toString()
        val interval = Constants.intervalList[spinner.selectedItemPosition]
        val timeUnit = Constants.timeUnitList[spinner.selectedItemPosition]

        if (searchText.trim().isNotEmpty()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val imageData = workDataOf(
                SearchWorker.SEARCH_TEXT to searchText
            )
            val searchWorkRequest =
                PeriodicWorkRequestBuilder<SearchWorker>(interval, timeUnit)
                    .setInputData(imageData)
                    .setInitialDelay(interval, timeUnit)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                AutoSearchNotification.channelName,
                ExistingPeriodicWorkPolicy.REPLACE,
                searchWorkRequest
            )
        } else WorkManager.getInstance(requireContext()).cancelAllWork()
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