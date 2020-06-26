package com.alvayonara.outsched.ui.schedule

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.alvayonara.outsched.utils.ConvertUtils
import com.alvayonara.outsched.utils.gone
import com.alvayonara.outsched.utils.invisible
import com.alvayonara.outsched.utils.visible
import kotlinx.android.synthetic.main.activity_select_schedule.*


class SelectScheduleActivity : AppCompatActivity() {

    private lateinit var scheduleViewModel: SelectScheduleViewModel

    private lateinit var scheduleAdapter: ScheduleAdapter

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
        getSchedulesData()
    }

    private fun initView() {
        scheduleAdapter = ScheduleAdapter()

        lyt_error.setOnClickListener {
            getSchedulesData()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.title = "Select Schedule"
    }

    private fun getSchedulesData() {
        main_content.invisible()
        lyt_error.invisible()
        progress_bar.visible()

        scheduleViewModel.setWeathersData(latitude, longitude)
        scheduleViewModel.getWeathersData().observe(this, Observer { schedules ->
            progress_bar.gone()

            if (schedules != null) {
                main_content.visible()

                scheduleAdapter.setSchedules(schedules)
                scheduleAdapter.notifyDataSetChanged()
            } else {
                lyt_error.visible()
            }
        })

        with(rv_schedule) {
            layoutManager = LinearLayoutManager(this@SelectScheduleActivity)
            adapter = scheduleAdapter
        }
    }

    private fun backAlertDialog() {
        val builder = AlertDialog.Builder(this@SelectScheduleActivity)

        builder.setTitle("Confirmation")
        builder.setMessage("Do you want to change location?")
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }

        builder.setNegativeButton("No") { dialog: DialogInterface, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    override fun onBackPressed() {
        backAlertDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                backAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        getSchedulesData()
    }
}