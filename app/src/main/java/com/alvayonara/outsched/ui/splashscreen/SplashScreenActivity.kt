package com.alvayonara.outsched.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.onboarding.OnBoardingActivity
import com.alvayonara.outsched.utils.ToolbarConfig

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