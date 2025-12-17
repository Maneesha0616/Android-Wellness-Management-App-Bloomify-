package com.bloomify.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.bloomify.R
import com.bloomify.utils.NotificationHelper
import com.bloomify.utils.PreferenceManager

class SettingsFragment : Fragment() {

    private lateinit var switchHydration: SwitchMaterial
    private lateinit var seekBarInterval: SeekBar
    private lateinit var tvInterval: TextView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableHydrationReminder()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
            switchHydration.isChecked = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        preferenceManager = PreferenceManager(requireContext())
        notificationHelper = NotificationHelper(requireContext())

        switchHydration = view.findViewById(R.id.switchHydration)
        seekBarInterval = view.findViewById(R.id.seekBarInterval)
        tvInterval = view.findViewById(R.id.tvInterval)

        loadSettings()

        switchHydration.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermission()
            } else {
                disableHydrationReminder()
            }
        }

        seekBarInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val hours = progress + 1
                tvInterval.text = "$hours hours"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val hours = (seekBar?.progress ?: 0) + 1
                preferenceManager.setHydrationInterval(hours)
                if (switchHydration.isChecked) {
                    notificationHelper.scheduleHydrationReminder(hours)
                    Toast.makeText(requireContext(), "Reminder updated", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    private fun loadSettings() {
        switchHydration.isChecked = preferenceManager.isHydrationEnabled()
        val interval = preferenceManager.getHydrationInterval()
        seekBarInterval.progress = interval - 1
        tvInterval.text = "$interval hours"
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    enableHydrationReminder()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            enableHydrationReminder()
        }
    }

    private fun enableHydrationReminder() {
        preferenceManager.setHydrationEnabled(true)
        val interval = preferenceManager.getHydrationInterval()
        notificationHelper.scheduleHydrationReminder(interval)
        Toast.makeText(requireContext(), "Hydration reminders enabled", Toast.LENGTH_SHORT).show()
    }

    private fun disableHydrationReminder() {
        preferenceManager.setHydrationEnabled(false)
        notificationHelper.cancelHydrationReminder()
        Toast.makeText(requireContext(), "Hydration reminders disabled", Toast.LENGTH_SHORT).show()
    }
}