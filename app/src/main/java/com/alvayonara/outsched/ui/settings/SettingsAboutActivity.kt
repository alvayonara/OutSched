package com.alvayonara.outsched.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_exercise_location.toolbar
import kotlinx.android.synthetic.main.activity_settings_about.*


class SettingsAboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_about)

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
        lyt_author.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/alva-yonara-puramandya/")
                )
            startActivity(intent)
        }

        iv_dark_sky.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/"))
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