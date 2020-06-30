package com.alvayonara.outsched.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity
import com.alvayonara.outsched.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initToolbar()
        initView()
    }

    private fun initToolbar() {
        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)

        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {

        }
    }

    private fun initView() {
        // init ViewPager TabLayout
        val sectionPageAdapter =
            DashboardSectionPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager_dashboard.adapter = sectionPageAdapter
        tabs_dashboard.setupWithViewPager(view_pager_dashboard)

        fab_add_schedule.setOnClickListener {
            val intent = Intent(this, ExerciseLocationActivity::class.java)
            startActivity(intent)
        }
    }
}