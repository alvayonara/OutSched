package com.alvayonara.outsched.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.databinding.ActivitySettingsBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_exercise_location.toolbar
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySettingsBinding
        get() = ActivitySettingsBinding::inflate

    override fun setup() {
        initToolbar()
        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)
    }

    private fun initView() {
        binding.lytAboutAppsSettings.setOnClickListener {
            val intent = Intent(this, SettingsAboutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}