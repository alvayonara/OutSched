package com.alvayonara.outsched.ui.schedule

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.R
import com.alvayonara.outsched.utils.gone
import com.alvayonara.outsched.utils.visible
import kotlinx.android.synthetic.main.activity_select_schedule.*


class SelectScheduleActivity : AppCompatActivity() {

    private lateinit var scheduleViewModel: SelectScheduleViewModel

    private lateinit var address: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    companion object {
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_schedule)

        initToolbar()

        scheduleViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SelectScheduleViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            address = extras.getString(EXTRA_ADDRESS)!!
            latitude = extras.getString(EXTRA_LATITUDE)!!
            longitude = extras.getString(EXTRA_LONGITUDE)!!
        }

        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.title = "Select Schedule"
    }

    private fun initView() {
        val scheduleAdapter = ScheduleAdapter()

//        lyt_schedule.gone()
//        lyt_no_connection.gone()
//
//        progress_bar.visible()
        scheduleViewModel.setWeathersData(latitude, longitude)
        scheduleViewModel.getWeathersData().observe(this, Observer { schedules ->
//            progress_bar.gone()

            if (schedules != null) {
//                lyt_schedule.visible()

                scheduleAdapter.setSchedules(schedules)
                scheduleAdapter.notifyDataSetChanged()
            } else {
//                lyt_no_connection.visible()
            }
        })

        with(rv_schedule) {
            layoutManager = LinearLayoutManager(this@SelectScheduleActivity)
            adapter = scheduleAdapter
        }
    }
}