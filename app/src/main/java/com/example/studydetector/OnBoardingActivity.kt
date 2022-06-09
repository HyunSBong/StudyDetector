package com.example.studydetector

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studydetector.onboarding.SplashingAdapter
import com.example.studydetector.onboarding.SplashingFirstFragment
import com.example.studydetector.onboarding.SplashingSecondFragment
import com.example.studydetector.onboarding.SplashingThirdFragment
import androidx.viewpager.widget.ViewPager


class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashing)
        var viewpager_bind = findViewById<ViewPager>(R.id.viewpager2)

        val onboardAdapter = SplashingAdapter(supportFragmentManager)

        onboardAdapter.addFragments(SplashingFirstFragment())
        onboardAdapter.addFragments(SplashingSecondFragment())
        onboardAdapter.addFragments(SplashingThirdFragment())

        viewpager_bind.adapter = onboardAdapter

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#FFFFFFFF")
        }
    }
}