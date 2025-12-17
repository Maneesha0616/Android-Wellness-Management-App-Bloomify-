package com.bloomify.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bloomify.R
import com.bloomify.utils.PreferenceManager

class SplashActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferenceManager = PreferenceManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun navigateToNextScreen() {
        val intent = when {
            !preferenceManager.isOnboardingCompleted() -> {
                Intent(this, OnboardingActivity::class.java)
            }
            !preferenceManager.isUserLoggedIn() -> {
                Intent(this, LoginActivity::class.java)
            }
            else -> {
                Intent(this, MainActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}