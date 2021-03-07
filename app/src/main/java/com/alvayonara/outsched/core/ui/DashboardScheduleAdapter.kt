package com.alvayonara.outsched.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.utils.ConvertUtils.dateTimeConvert
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_DATE_WITH_DAY
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_ONLY_TIME
import com.alvayonara.outsched.databinding.ItemRowDashboardScheduleBinding
import com.alvayonara.outsched.ui.schedule.ScheduleDetailDialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DashboardScheduleAdapter :
    RecyclerView.Adapter<DashboardScheduleAdapter.DashboardScheduleViewHolder>() {

    private var listItem: MutableList<Schedule> = mutableListOf()

    fun setSchedules(schedules: List<Schedule>?) {
        if (schedules == null) return
        listItem.clear()
        listItem.addAll(schedules)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardScheduleViewHolder = DashboardScheduleViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_dashboard_schedule, parent, false
        )
    )

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(
        holder: DashboardScheduleViewHolder,
        position: Int
    ) = holder.bindItem(listItem[position])

    class DashboardScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowDashboardScheduleBinding.bind(itemView)
        fun bindItem(schedule: Schedule) {
            with(itemView) {
                schedule.let {
                    with(binding) {
                        val mMapView =
                            mapDashboard
                        MapsInitializer.initialize(context)
                        mMapView.onCreate(null)
                        mMapView.onResume()
                        mMapView.getMapAsync { googleMap ->
                            googleMap.uiSettings.setAllGesturesEnabled(false)
                            val savedLatLng = LatLng(
                                it.latitude.toDouble(),
                                it.longitude.toDouble()
                            )
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 18f))
                            googleMap.addMarker(MarkerOptions().position(savedLatLng))
                        }

                        tvWeather.text = it.summary
                        tvHour.text = it.time.dateTimeConvert(FORMAT_ONLY_TIME)
                        tvDate.text = it.time.dateTimeConvert(FORMAT_DATE_WITH_DAY)

                        itemView.setOnClickListener {
                            val scheduleDetailDialogFragment = ScheduleDetailDialogFragment()

                            val mBundle = Bundle().apply {
                                putParcelable(
                                    ScheduleDetailDialogFragment.EXTRA_SCHEDULE_DETAIL,
                                    schedule
                                )
                            }

                            scheduleDetailDialogFragment.arguments = mBundle
                            scheduleDetailDialogFragment.show(
                                (context as AppCompatActivity).supportFragmentManager,
                                ScheduleDetailDialogFragment::class.java.simpleName
                            )
                        }
                    }
                }
            }
        }
    }
}
