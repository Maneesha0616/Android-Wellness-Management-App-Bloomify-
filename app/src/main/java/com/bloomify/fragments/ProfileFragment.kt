package com.bloomify.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.bloomify.R
import com.bloomify.activities.LoginActivity
import com.bloomify.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvMemberSince: TextView
    private lateinit var tvTotalHabits: TextView
    private lateinit var tvTotalMoods: TextView
    private lateinit var btnLogout: MaterialButton
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        preferenceManager = PreferenceManager(requireContext())

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        tvMemberSince = view.findViewById(R.id.tvMemberSince)
        tvTotalHabits = view.findViewById(R.id.tvTotalHabits)
        tvTotalMoods = view.findViewById(R.id.tvTotalMoods)
        btnLogout = view.findViewById(R.id.btnLogout)

        loadUserProfile()

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        return view
    }

    private fun loadUserProfile() {
        val user = preferenceManager.getUser()
        user?.let {
            tvUserName.text = it.name
            tvUserEmail.text = it.email
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            tvMemberSince.text = "Member since ${dateFormat.format(Date(it.joinedDate))}"
        }

        val habits = preferenceManager.getHabits()
        val moods = preferenceManager.getMoodEntries()
        tvTotalHabits.text = "${habits.size} habits tracked"
        tvTotalMoods.text = "${moods.size} mood entries logged"
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                preferenceManager.logout()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}