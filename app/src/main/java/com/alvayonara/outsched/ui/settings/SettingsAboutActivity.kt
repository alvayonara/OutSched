package com.alvayonara.outsched.ui.settings

import android.view.LayoutInflater
import android.view.MenuItem
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.utils.Helper.intentUri
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.databinding.ActivitySettingsAboutBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_exercise_location.*


class SettingsAboutActivity : BaseActivity<ActivitySettingsAboutBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySettingsAboutBinding
        get() = ActivitySettingsAboutBinding::inflate

    override fun setup() {
        initToolbar()
        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "About Apps"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ToolbarConfig.setSystemBarColor(this, R.color.colorGreen)
    }

    private fun initView() {
        binding.lytAuthor.setOnClickListener {
            intentUri(this, "https://www.linkedin.com/in/alva-yonara-puramandya/")
        }

        binding.ivDarkSky.setOnClickListener {
            intentUri(this, "https://darksky.net/poweredby/")
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