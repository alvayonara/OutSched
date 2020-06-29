package com.alvayonara.outsched.ui.schedule

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.local.entity.item.ScheduleListItem.Companion.TYPE_DATE
import com.alvayonara.outsched.data.local.entity.item.ScheduleListItem.Companion.TYPE_GENERAL
import com.alvayonara.outsched.data.local.entity.item.DateItem
import com.alvayonara.outsched.data.local.entity.item.ScheduleItem
import com.alvayonara.outsched.data.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.utils.ConvertUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_select_schedule.*
import kotlinx.android.synthetic.main.item_date_section.view.*
import kotlinx.android.synthetic.main.item_row_select_schedule.view.iv_weather
import kotlinx.android.synthetic.main.item_row_select_schedule.view.tv_hour
import kotlinx.android.synthetic.main.item_row_select_schedule.view.tv_temperature
import kotlinx.android.synthetic.main.item_row_select_schedule.view.tv_weather


class ScheduleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem: MutableList<ScheduleListItem> = mutableListOf()

    fun setSchedules(schedules: List<ScheduleListItem>?) {
        if (schedules == null) return
        listItem.clear()
        listItem.addAll(schedules)
    }

    override fun getItemViewType(position: Int): Int = listItem[position].getType()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_GENERAL -> {
                ScheduleViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_select_schedule, parent, false)
                )
            }
            TYPE_DATE -> {
                DateViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_date_section, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            TYPE_GENERAL -> {
                val holder: ScheduleViewHolder = viewHolder as ScheduleViewHolder
                holder.bindItem(listItem[position] as ScheduleItem)
            }
            TYPE_DATE -> {
                val holder: DateViewHolder = viewHolder as DateViewHolder
                holder.bindItem(listItem[position] as DateItem)
            }
        }
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(schedule: ScheduleItem) {
            with(itemView) {
                setImageWeather(schedule, iv_weather)
                setScheduleView(schedule, tv_weather, tv_hour, tv_temperature)

                itemView.setOnClickListener {
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                    dialog.setContentView(R.layout.dialog_select_schedule)
                    dialog.window
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(true)

                    val mMapView =
                        dialog.map_detail as MapView
                    MapsInitializer.initialize(context)

                    mMapView.onCreate(dialog.onSaveInstanceState())
                    mMapView.onResume()

                    mMapView.getMapAsync { googleMap ->
                        googleMap.uiSettings.setAllGesturesEnabled(false)
                        val savedLatLng = LatLng(
                            schedule.scheduleEntity!!.latitude!!.toDouble(),
                            schedule.scheduleEntity!!.longitude!!.toDouble()
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, 18f))
                        googleMap.addMarker(MarkerOptions().position(savedLatLng))
                    }

                    setImageWeather(schedule, dialog.iv_weather)
                    setScheduleView(
                        schedule,
                        dialog.tv_weather,
                        dialog.tv_hour,
                        dialog.tv_temperature
                    )
                    dialog.tv_date.text =
                        ConvertUtils.convertTimeToDate(schedule.scheduleEntity!!.time)

                    dialog.btn_save.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.btn_cancel.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
                }
            }
        }

        private fun setImageWeather(schedule: ScheduleItem, ivWeather: ImageView) {
            when {
                schedule.scheduleEntity!!.icon!!.startsWith("clear") -> {
                    ivWeather.setImageResource(R.drawable.ic_clear)
                }
                schedule.scheduleEntity!!.icon!!.startsWith("cloudy") -> {
                    ivWeather.setImageResource(R.drawable.ic_clouds)
                }
                schedule.scheduleEntity!!.icon!!.startsWith("partly-cloudy") -> {
                    ivWeather.setImageResource(R.drawable.ic_partly_cloudy)
                }
                schedule.scheduleEntity!!.icon!!.contains("rain") -> {
                    ivWeather.setImageResource(R.drawable.ic_rain)
                }
            }
        }

        private fun setScheduleView(
            schedule: ScheduleItem,
            tvWeather: TextView,
            tvHour: TextView,
            tvTemperature: TextView
        ) {
            with(itemView) {
                tvWeather.text = schedule.scheduleEntity!!.summary
                tvHour.text =
                    ConvertUtils.convertTimeToHour(schedule.scheduleEntity!!.time)
                tvTemperature.text = context.getString(
                    R.string.temperature,
                    ConvertUtils.convertTemperatureRound(schedule.scheduleEntity!!.temperature)
                        .toString()
                )
            }
        }
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(date: DateItem) {
            with(itemView) {
                tv_section_date.text = date.dateList
            }
        }
    }
}