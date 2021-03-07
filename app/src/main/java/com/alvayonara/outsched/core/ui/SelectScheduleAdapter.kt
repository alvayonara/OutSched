package com.alvayonara.outsched.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.domain.model.item.DateItem
import com.alvayonara.outsched.core.domain.model.item.ScheduleItem
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem.Companion.TYPE_DATE
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem.Companion.TYPE_GENERAL
import com.alvayonara.outsched.core.utils.ConvertUtils.convertTemperatureRound
import com.alvayonara.outsched.ui.schedule.ScheduleDetailDialogFragment.Companion.EXTRA_SCHEDULE_DETAIL
import com.alvayonara.outsched.core.utils.ConvertUtils.dateTimeConvert
import com.alvayonara.outsched.core.utils.DateFormat
import com.alvayonara.outsched.databinding.ItemDateSectionBinding
import com.alvayonara.outsched.databinding.ItemRowSelectScheduleBinding
import com.alvayonara.outsched.ui.schedule.ScheduleDetailDialogFragment


class SelectScheduleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem: MutableList<ScheduleListItem> = mutableListOf()

    fun setSchedules(schedules: List<ScheduleListItem>?) {
        if (schedules == null) return
        listItem.clear()
        listItem.addAll(schedules)
        notifyDataSetChanged()
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
        private val binding = ItemRowSelectScheduleBinding.bind(itemView)
        fun bindItem(schedule: ScheduleItem) {
            with(itemView) {
                schedule.schedule?.let {
                    with(binding) {
                        when {
                            it.icon.startsWith("clear") -> ivWeather.setImageResource(R.drawable.ic_clear)
                            it.icon.startsWith("cloudy") -> ivWeather.setImageResource(R.drawable.ic_clouds)
                            it.icon.startsWith("partly-cloudy") -> ivWeather.setImageResource(R.drawable.ic_partly_cloudy)
                            it.icon.contains("rain") -> ivWeather.setImageResource(R.drawable.ic_rain)
                        }

                        tvWeather.text = it.summary
                        tvHour.text =
                            it.time.dateTimeConvert(DateFormat.FORMAT_ONLY_TIME)
                        tvTemperature.text = context.getString(
                            R.string.temperature,
                            it.temperature.convertTemperatureRound()
                        )

                        itemView.setOnClickListener {
                            val scheduleDetailDialogFragment = ScheduleDetailDialogFragment()

                            val mBundle = Bundle()
                            mBundle.putParcelable(EXTRA_SCHEDULE_DETAIL, schedule.schedule)

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

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemDateSectionBinding.bind(itemView)
        fun bindItem(date: DateItem) {
            with(itemView) {
                binding.tvSectionDate.text = date.dateList
            }
        }
    }
}