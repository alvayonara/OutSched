package com.alvayonara.outsched.ui.schedule

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.ui.dashboard.DashboardActivity
import com.alvayonara.outsched.utils.ConvertUtils
import com.alvayonara.outsched.utils.visible
import com.alvayonara.outsched.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_select_schedule.*


class ScheduleDetailDialogFragment : DialogFragment() {

    private lateinit var scheduleDetailViewModel: ScheduleDetailViewModel
    private lateinit var mapFragment: SupportMapFragment

    companion object {
        const val EXTRA_SCHEDULE_DETAIL = "extra_schedule_detail"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_select_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        scheduleDetailViewModel =
            ViewModelProvider(requireActivity(), factory)[ScheduleDetailViewModel::class.java]

        if (arguments != null) {
            val schedule = arguments!!.getParcelable<ScheduleEntity>(EXTRA_SCHEDULE_DETAIL)
            initView(schedule)
        }
    }

    private fun initView(schedule: ScheduleEntity?) {
        mapFragment =
            (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.map_detail) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.setAllGesturesEnabled(false)
            val savedLatLng = LatLng(
                schedule!!.latitude!!.toDouble(),
                schedule.longitude!!.toDouble()
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 18f))
            googleMap.addMarker(MarkerOptions().position(savedLatLng))
        }

        when {
            schedule!!.icon!!.startsWith("clear") -> {
                iv_weather.setImageResource(R.drawable.ic_clear)
            }
            schedule.icon!!.startsWith("cloudy") -> {
                iv_weather.setImageResource(R.drawable.ic_clouds)
            }
            schedule.icon!!.startsWith("partly-cloudy") -> {
                iv_weather.setImageResource(R.drawable.ic_partly_cloudy)
            }
            schedule.icon!!.contains("rain") -> {
                iv_weather.setImageResource(R.drawable.ic_rain)
            }
        }
        tv_weather.text = schedule!!.summary
        tv_date.text = ConvertUtils.convertTimeToDate(schedule.time)
        tv_hour.text = ConvertUtils.convertTimeToHour(schedule.time)
        tv_temperature.text = getString(
            R.string.temperature,
            ConvertUtils.convertTemperatureRound(schedule.temperature)
                .toString()
        )

        // If schedule  then schedule already stored in database
//        if (checkSchedule(schedule)) {
//
//        } else {
//        }

        checkSchedule(schedule)
    }

    private fun checkSchedule(schedule: ScheduleEntity) {
        scheduleDetailViewModel.checkScheduleData(
            schedule.id,
            schedule.latitude!!, schedule.longitude!!
        ).observe(viewLifecycleOwner, Observer { result ->
            Log.d("live", result.toString())

            if (result) {
                // Set layout (delete or change schedule)
                lyt_dialog_saved_schedule.visible()

                // Change schedule button
                btn_change.setOnClickListener {
                    dismiss()
                }

                // Delete schedule button
                btn_delete.setOnClickListener {
                    dismiss()
                }
            } else {
                // Set layout (cancel or save schedule)
                lyt_dialog_select_schedule.visible()

                // Save new schedule button
                btn_save.setOnClickListener {
                    // Insert schedule to database (new data)
                    scheduleDetailViewModel.insert(schedule)

                    val intent = Intent(requireActivity(), DashboardActivity::class.java)
                    startActivity(intent)
                    (context as AppCompatActivity).finishAffinity()

                    // Show notification (schedule already saved)
                    showNotification(schedule)
                }

                btn_cancel.setOnClickListener {
                    dismiss()
                }
            }
        })
    }

    private fun showNotification(schedule: ScheduleEntity) {

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .remove(mapFragment).commitAllowingStateLoss()
    }
}