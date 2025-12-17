package com.bloomify.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.bloomify.R
import com.bloomify.fragments.HabitsFragment
import com.bloomify.fragments.MoodJournalFragment
import com.bloomify.fragments.ProfileFragment
import com.bloomify.fragments.SettingsFragment
import java.text.SimpleDateFormat
import java.util.*
import com.bloomify.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceManager = PreferenceManager(this)
        checkAndResetDailyHabits()

        bottomNav = findViewById(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            loadFragment(HabitsFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_habits -> HabitsFragment()
                R.id.nav_mood -> MoodJournalFragment()
                R.id.nav_settings -> SettingsFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HabitsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun checkAndResetDailyHabits() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastResetDate = preferenceManager.getLastResetDate()

        if (currentDate != lastResetDate) {
            val habits = preferenceManager.getHabits()
            // Fixed: Use for loop instead of forEach to avoid ambiguity
            for (habit in habits) {
                habit.isCompleted = false

            }
            preferenceManager.saveHabits(habits)
            preferenceManager.setLastResetDate(currentDate)
        }
    }
}