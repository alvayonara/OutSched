package com.alvayonara.outsched.ui.splashscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }
}