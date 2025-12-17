package com.bloomify.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bloomify.R

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val pages = listOf(
        OnboardingPage(
            R.drawable.ic_onboarding_1,
            "Track Your Habits",
            "Build healthy routines and track your daily progress"
        ),
        OnboardingPage(
            R.drawable.ic_onboarding_2,
            "Log Your Mood",
            "Keep a journal of your emotions and see patterns over time"
        ),
        OnboardingPage(
            R.drawable.ic_onboarding_3,
            "Stay Hydrated",
            "Get reminders to drink water throughout the day"
        )
    )

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingFragment.newInstance(pages[position])
    }

    data class OnboardingPage(
        val imageRes: Int,
        val title: String,
        val description: String
    )

    class OnboardingFragment : Fragment() {
        companion object {
            private const val ARG_PAGE = "page"

            fun newInstance(page: OnboardingPage): OnboardingFragment {
                val fragment = OnboardingFragment()
                val args = Bundle()
                args.putInt("imageRes", page.imageRes)
                args.putString("title", page.title)
                args.putString("description", page.description)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_onboarding_page, container, false)

            val imageView: ImageView = view.findViewById(R.id.ivOnboarding)
            val tvTitle: TextView = view.findViewById(R.id.tvOnboardingTitle)
            val tvDescription: TextView = view.findViewById(R.id.tvOnboardingDescription)


            arguments?.let {
                imageView.setImageResource(it.getInt("imageRes"))
                tvTitle.text = it.getString("title")
                tvDescription.text = it.getString("description")
            }

            return view
        }
    }
}