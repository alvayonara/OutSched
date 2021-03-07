package com.alvayonara.outsched.ui.splashscreen

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.databinding.ActivitySplashScreenBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import com.alvayonara.outsched.ui.onboarding.OnBoardingActivity

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySplashScreenBinding
        get() = ActivitySplashScreenBinding::inflate

    override fun setup() {
        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}