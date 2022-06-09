package com.example.studydetector.onboarding


import android.content.Intent
import android.os.Bundle
//import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.studydetector.MainActivity

import com.example.studydetector.R

class SplashingThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?):View? {
        var v:View = inflater.inflate(R.layout.fragment_splash_third, container, false)
        var btn_bind = v.findViewById<Button>(R.id.btnOnBoarding)
        btn_bind.setOnClickListener {
            val intent = Intent(this.context, MainActivity::class.java)
            activity?.finish()

            startActivity(intent)
        }
        return v
    }

}
