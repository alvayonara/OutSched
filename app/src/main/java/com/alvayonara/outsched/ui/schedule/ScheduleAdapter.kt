package com.alvayonara.outsched.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.alvayonara.outsched.utils.ConvertUtils
import kotlinx.android.synthetic.main.item_row_choose_schedule.view.*

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private var listSchedule = ArrayList<ScheduleEntity>()

    fun setSchedules(schedules: List<ScheduleEntity>?) {
        if (schedules == null) return
        listSchedule.clear()
        listSchedule.addAll(schedules)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder = ScheduleViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_choose_schedule, parent, false)
    )

    override fun getItemCount(): Int = listSchedule.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) =
        holder.bindItem(listSchedule[position])

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(schedule: ScheduleEntity) {
            with(itemView) {
                when {
                    schedule.icon.startsWith("clear") -> {
                        iv_weather.setImageResource(R.drawable.ic_clear)
                    }
                    schedule.icon.startsWith("cloudy") -> {
                        iv_weather.setImageResource(R.drawable.ic_clouds)
                    }
                    schedule.icon.startsWith("partly-cloudy") -> {
                        iv_weather.setImageResource(R.drawable.ic_partly_cloudy)
                    }
                    schedule.icon.contains("rain") -> {
                        iv_weather.setImageResource(R.drawable.ic_rain)
                    }
                }

                tv_weather.text = schedule.summary
                tv_date.text = ConvertUtils.convertTimeToDate(schedule.time)
                tv_hour.text = ConvertUtils.convertTimeToHour(schedule.time)
                tv_temperature.text =
                    context.getString(
                        R.string.temperature,
                        ConvertUtils.convertTemperatureRound(schedule.temperature).toString()
                    )
            }
        }
    }
}