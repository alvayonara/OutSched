package com.alvayonara.outsched.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.onboarding.OnBoardingActivity
import com.alvayonara.outsched.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)

        // Set interval display splash screen 2 sec
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}