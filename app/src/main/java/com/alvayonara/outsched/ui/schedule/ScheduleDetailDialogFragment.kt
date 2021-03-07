package com.alvayonara.outsched.ui.schedule

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.alvayonara.outsched.MyApplication
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.receiver.ScheduleReminderReceiver
import com.alvayonara.outsched.core.ui.ViewModelFactory
import com.alvayonara.outsched.core.utils.ConvertUtils.convertTemperatureRound
import com.alvayonara.outsched.core.utils.ConvertUtils.dateTimeConvert
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_DATE_WITH_DAY
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_ONLY_TIME
import com.alvayonara.outsched.core.utils.Helper.showMaterialDialog
import com.alvayonara.outsched.core.utils.gone
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.DialogSelectScheduleBinding
import com.alvayonara.outsched.ui.base.BaseDialogFragment
import com.alvayonara.outsched.ui.dashboard.DashboardActivity
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity.Companion.EXTRA_ID
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_REQUEST_CODE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_select_schedule.*
import javax.inject.Inject

class ScheduleDetailDialogFragment : BaseDialogFragment<DialogSelectScheduleBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val scheduleDetailViewModel: ScheduleDetailViewModel by viewModels {
        factory
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var scheduleReminderReceiver: ScheduleReminderReceiver

    companion object {
        const val EXTRA_SCHEDULE_DETAIL = "extra_schedule_detail"
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogSelectScheduleBinding
        get() = DialogSelectScheduleBinding::inflate

    override fun injector() {
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun setup() {
        if (arguments != null) {
            initView(requireArguments().getParcelable(EXTRA_SCHEDULE_DETAIL))
        }
        scheduleReminderReceiver = ScheduleReminderReceiver()
    }

    private fun initView(schedule: Schedule?) {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map_detail) as SupportMapFragment

        schedule?.let {
            Log.d("schedd", schedule.toString())
            mapFragment.getMapAsync { googleMap ->
                googleMap.uiSettings.setAllGesturesEnabled(false)
                val savedLatLng = LatLng(
                    it.latitude.toDouble(),
                    it.longitude.toDouble()
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 18f))
                googleMap.addMarker(MarkerOptions().position(savedLatLng))
            }

            when {
                it.icon.startsWith("clear") -> binding.ivWeather.setImageResource(R.drawable.ic_clear)
                it.icon.startsWith("cloudy") -> binding.ivWeather.setImageResource(R.drawable.ic_clouds)
                it.icon.startsWith("partly-cloudy") -> binding.ivWeather.setImageResource(R.drawable.ic_partly_cloudy)
                it.icon.contains("rain") -> binding.ivWeather.setImageResource(R.drawable.ic_rain)
            }
            binding.tvWeather.text = it.summary
            binding.tvDate.text = it.time.dateTimeConvert(FORMAT_DATE_WITH_DAY)
            binding.tvHour.text = it.time.dateTimeConvert(FORMAT_ONLY_TIME)
            binding.tvTemperature.text = getString(
                R.string.temperature,
                it.temperature.convertTemperatureRound()
            )

            checkSchedule(it)
        }
    }

    private fun checkSchedule(schedule: Schedule) {
        scheduleDetailViewModel.checkScheduleData(
            schedule.time,
            schedule.latitude,
            schedule.longitude
        ).observe(viewLifecycleOwner, { result ->
            if (result > 0) {
                if (schedule.time * 1000 < System.currentTimeMillis()) {
                    btn_change.gone()
                }

                // Set layout (delete or change schedule)
                binding.lytDialogSavedSchedule.visible()

                // Change schedule button
                binding.btnChange.setOnClickListener {
                    showMaterialDialog(
                        context = requireActivity(),
                        title = R.string.dialog_confirmation,
                        message = R.string.change_schedule_dialog,
                        positiveText = R.string.dialog_yes,
                        negativeText = R.string.dialog_no,
                        actionPositive = {
                            dismiss()

                            val intent = Intent(
                                    requireActivity(),
                                    ExerciseLocationActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_ID, schedule.id)
                                    putExtra(EXTRA_REQUEST_CODE, schedule.requestCode)
                                }
                            startActivity(intent)
                        },
                        actionNegative = { dismiss() }
                    )
                }

                // Delete schedule button
                binding.btnDelete.setOnClickListener {
                    showMaterialDialog(
                        context = requireActivity(),
                        title = R.string.dialog_confirmation,
                        message = R.string.delete_schedule_dialog,
                        positiveText = R.string.dialog_yes,
                        negativeText = R.string.dialog_no,
                        actionPositive = {
                            dismiss()

                            // Cancel alarm
                            scheduleReminderReceiver.cancelAlarm(
                                requireActivity(),
                                schedule.requestCode
                            )

                            scheduleDetailViewModel.delete(schedule)

                            Toast.makeText(
                                requireActivity(),
                                "Schedule successfully deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        actionNegative = { dismiss() }
                    )
                }
            } else {
                // Set layout (cancel or save schedule)
                binding.lytDialogSelectSchedule.visible()

                // Save new schedule button
                binding.btnSave.setOnClickListener {
                    dismiss()

                    // If request code is 0 then the schedule are not saved yet on db
                    if (schedule.requestCode == 0) {
                        // Set new request code
                        schedule.requestCode = System.currentTimeMillis().toInt()
                    }

                    scheduleDetailViewModel.insert(schedule)

                    // Set alarm
                    scheduleReminderReceiver.setScheduleReminder(requireActivity(), schedule)

                    val intent = Intent(requireActivity(), DashboardActivity::class.java)
                    startActivity(intent)
                    (context as AppCompatActivity).finishAffinity()

                    Toast.makeText(
                        requireActivity(),
                        "Schedule successfully saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.btnCancel.setOnClickListener { dismiss() }
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}