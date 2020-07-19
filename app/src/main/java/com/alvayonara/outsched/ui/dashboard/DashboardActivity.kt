package com.alvayonara.outsched.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity
import com.alvayonara.outsched.ui.settings.SettingsActivity
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
        view_pager_dashboard.adapter = sectionPageAdapter
        tabs_dashboard.setupWithViewPager(view_pager_dashboard)

        fab_add_schedule.setOnClickListener {
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