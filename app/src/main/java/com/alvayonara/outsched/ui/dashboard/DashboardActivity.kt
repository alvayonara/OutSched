package com.alvayonara.outsched.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.ui.DashboardSectionPagerAdapter
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity
import com.alvayonara.outsched.ui.settings.SettingsActivity
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.databinding.ActivityDashboardBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun setup() {
        initToolbar()
        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)
    }

    private fun initView() {
        // init ViewPager TabLayout
        val sectionPageAdapter =
            DashboardSectionPagerAdapter(
                this,
                supportFragmentManager
            )

        binding.viewPagerDashboard.adapter = sectionPageAdapter
        binding.tabsDashboard.setupWithViewPager(view_pager_dashboard)
        binding.fabAddSchedule.setOnClickListener {
            val intent = Intent(this, ExerciseLocationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}