package com.alvayonara.outsched.ui.onboarding

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.dashboard.DashboardActivity
import com.alvayonara.outsched.utils.Preferences
import com.alvayonara.outsched.utils.Preferences.Companion.ON_BOARDING
import com.alvayonara.outsched.utils.ToolbarConfig
import com.alvayonara.outsched.utils.gone
import com.alvayonara.outsched.utils.visible
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    companion object {
        private const val TOTAL_STEPPER = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)

        preferences = Preferences(this)

        initView()
    }

    private fun initView() {
        // Init dots position 0
        initDots(0)

        checkOnBoardingStatus()

        val pagerAdapter = OnBoardingPagerAdapter(this)
        view_pager_on_boarding.adapter = pagerAdapter
        view_pager_on_boarding.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_get_started.gone()
        btn_get_started.setOnClickListener {
            preferences.setValues(ON_BOARDING, "1")
            toDashboardActivity()
        }

        btn_skip.setOnClickListener {
            preferences.setValues(ON_BOARDING, "1")
            toDashboardActivity()
        }
    }

    private fun checkOnBoardingStatus() {
        if (preferences.getValues(ON_BOARDING).equals("1")) {
            toDashboardActivity()
        }
    }

    private fun toDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initDots(index: Int) {
        ll_dots.removeAllViews()

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

            ll_dots.addView(dots[i])
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

            // Position start from 0
            // 0 -> dot 1
            // 1 -> dot 2
            // 2 -> dot 3
            // 3 -> last dot (dot 4)
            // set visibility based on position
            if (position == 3) {
                // Set get started button to visible
                btn_get_started.visible()
            } else {
                // Hide/set to gone get started button
                btn_get_started.gone()
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }
}