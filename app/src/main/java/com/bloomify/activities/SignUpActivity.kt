package com.bloomify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.bloomify.R
import com.bloomify.models.User
import com.bloomify.utils.PreferenceManager

class SignUpActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: MaterialButton
    private lateinit var tvLogin: TextView
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        preferenceManager = PreferenceManager(this)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLogin = findViewById(R.id.tvLogin)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {
                val user = User(name, email, password)
                preferenceManager.saveUser(user)
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty()) {
            etName.error = "Name required"
            return false
        }
        if (email.isEmpty()) {
            etEmail.error = "Email required"
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Password required"
            return false
        }
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return false
        }
        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords don't match"
            return false
        }
        return true
    }
}