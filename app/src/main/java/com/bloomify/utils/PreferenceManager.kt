package com.bloomify.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.bloomify.models.Habit
import com.bloomify.models.MoodEntry
import com.bloomify.models.User

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("BloomifyPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Onboarding & Auth
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean("onboarding_completed", false)
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("user_logged_in", false)
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("user_logged_in", loggedIn).apply()
    }

    // User Data
    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString("current_user", userJson).apply()
        setUserLoggedIn(true)
    }

    fun getUser(): User? {
        val userJson = prefs.getString("current_user", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else null
    }

    fun logout() {
        setUserLoggedIn(false)
        prefs.edit().remove("current_user").apply()
    }

    // Habits
    fun saveHabits(habits: List<Habit>) {
        val habitsJson = gson.toJson(habits)
        prefs.edit().putString("habits", habitsJson).apply()
    }

    fun getHabits(): MutableList<Habit> {
        val habitsJson = prefs.getString("habits", null)
        return if (habitsJson != null) {
            val type = object : TypeToken<MutableList<Habit>>() {}.type
            gson.fromJson(habitsJson, type)
        } else {
            mutableListOf()
        }
    }

    // Mood Entries
    fun saveMoodEntries(moods: List<MoodEntry>) {
        val moodsJson = gson.toJson(moods)
        prefs.edit().putString("mood_entries", moodsJson).apply()
    }

    fun getMoodEntries(): MutableList<MoodEntry> {
        val moodsJson = prefs.getString("mood_entries", null)
        return if (moodsJson != null) {
            val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
            gson.fromJson(moodsJson, type)
        } else {
            mutableListOf()
        }
    }

    // Hydration Reminder Settings
    fun setHydrationInterval(hours: Int) {
        prefs.edit().putInt("hydration_interval", hours).apply()
    }

    fun getHydrationInterval(): Int {
        return prefs.getInt("hydration_interval", 2)
    }

    fun setHydrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("hydration_enabled", enabled).apply()
    }

    fun isHydrationEnabled(): Boolean {
        return prefs.getBoolean("hydration_enabled", false)
    }

    // Daily Reset Check
    fun getLastResetDate(): String {
        return prefs.getString("last_reset_date", "") ?: ""
    }

    fun setLastResetDate(date: String) {
        prefs.edit().putString("last_reset_date", date).apply()
    }
}