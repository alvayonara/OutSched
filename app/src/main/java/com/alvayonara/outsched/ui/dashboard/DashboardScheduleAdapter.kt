package com.alvayonara.outsched.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.ui.schedule.ScheduleDetailDialogFragment
import com.alvayonara.outsched.utils.ConvertUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.item_row_dashboard_schedule.view.*

class DashboardScheduleAdapter :
    RecyclerView.Adapter<DashboardScheduleAdapter.DashboardScheduleViewHolder>() {

    private var listItem: MutableList<ScheduleEntity> = mutableListOf()

    fun setSchedules(schedules: List<ScheduleEntity>?) {
        if (schedules == null) return
        listItem.clear()
        listItem.addAll(schedules)
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
        fun bindItem(scheduleEntity: ScheduleEntity) {
            with(itemView) {
                val mMapView =
                    map_dashboard as MapView
                MapsInitializer.initialize(context)

                mMapView.onCreate(null)
                mMapView.onResume()

                mMapView.getMapAsync { googleMap ->
                    googleMap.uiSettings.setAllGesturesEnabled(false)
                    val savedLatLng = LatLng(
                        scheduleEntity.latitude!!.toDouble(),
                        scheduleEntity.longitude!!.toDouble()
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 18f))
                    googleMap.addMarker(MarkerOptions().position(savedLatLng))
                }

                tv_weather.text = scheduleEntity.summary
                tv_hour.text = ConvertUtils.convertTimeToHour(scheduleEntity.time)
                tv_date.text = ConvertUtils.convertTimeToDate(scheduleEntity.time)

                itemView.setOnClickListener {
                    val scheduleDetailDialogFragment = ScheduleDetailDialogFragment()

                    val mBundle = Bundle()
                    mBundle.putParcelable(
                        ScheduleDetailDialogFragment.EXTRA_SCHEDULE_DETAIL,
                        scheduleEntity
                    )

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
