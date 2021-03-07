package com.alvayonara.outsched.ui.onboarding

import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.ui.OnBoardingPagerAdapter
import com.alvayonara.outsched.core.utils.Preferences
import com.alvayonara.outsched.core.utils.Preferences.Companion.ON_BOARDING
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.core.utils.gone
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.ActivityOnBoardingBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import com.alvayonara.outsched.ui.dashboard.DashboardActivity

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>() {

    private lateinit var preferences: Preferences
    companion object {
        private const val TOTAL_STEPPER = 4
    }

    override val bindingInflater: (LayoutInflater) -> ActivityOnBoardingBinding
        get() = ActivityOnBoardingBinding::inflate

    override fun setup() {
        ToolbarConfig.setSystemBarColor(this, android.R.color.white)
        ToolbarConfig.setSystemBarLight(this)
        preferences = Preferences(this)
        initView()
    }

    private fun initView() {
        // Init dots position 0
        initDots(0)
        checkOnBoardingStatus()

        val pagerAdapter = OnBoardingPagerAdapter(this)
        binding.viewPagerOnBoarding.adapter = pagerAdapter
        binding.viewPagerOnBoarding.addOnPageChangeListener(viewPagerPageChangeListener)

        binding.btnGetStarted.gone()
        binding.btnGetStarted.setOnClickListener {
            preferences.setValues(ON_BOARDING, "1")
            toDashboardActivity()
        }

        binding.btnSkip.setOnClickListener {
            preferences.setValues(ON_BOARDING, "1")
            toDashboardActivity()
        }
    }

    private fun checkOnBoardingStatus() {
        if (preferences.getValues(ON_BOARDING).equals("1")) toDashboardActivity()
    }

    private fun toDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initDots(index: Int) {
        binding.llDots.removeAllViews()

        val dots =
            arrayOfNulls<ImageView>(TOTAL_STEPPER)

        for (i in dots.indices) {
            dots[i] = ImageView(this)

            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams(15, 15))
            params.setMargins(10, 10, 10, 10)

            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(
                ContextCompat.getColor(this, R.color.colorDotsDark),
                PorterDuff.Mode.SRC_IN
            )

            binding.llDots.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[index]?.setImageResource(R.drawable.shape_circle)
            dots[index]?.setColorFilter(
                ContextCompat.getColor(this, R.color.colorDotsWhite),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            initDots(position)
            if (position == 3) {
                // Set get started button to visible
                binding.btnGetStarted.visible()
            } else {
                // Hide/set to gone get started button
                binding.btnGetStarted.gone()
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }
}