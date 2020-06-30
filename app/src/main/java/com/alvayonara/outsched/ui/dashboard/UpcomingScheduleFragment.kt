package com.alvayonara.outsched.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.schedule.SelectScheduleAdapter
import com.alvayonara.outsched.ui.schedule.SelectScheduleViewModel
import com.alvayonara.outsched.utils.gone
import com.alvayonara.outsched.utils.visible
import com.alvayonara.outsched.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_upcoming_schedule.*

class UpcomingScheduleFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_upcoming_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        dashboardViewModel =
            ViewModelProvider(requireActivity(), factory)[DashboardViewModel::class.java]

        getUpcomingSchedule()
    }

    private fun getUpcomingSchedule() {
        val dashboardScheduleAdapter = DashboardScheduleAdapter()

        progress_bar_upcoming_schedule.visible()

        dashboardViewModel.getUpcomingSchedules()
            .observe(viewLifecycleOwner, Observer { schedules ->
                progress_bar_upcoming_schedule.gone()

                if (schedules.isNotEmpty()) {
                    dashboardScheduleAdapter.setSchedules(schedules)
                    dashboardScheduleAdapter.notifyDataSetChanged()
                } else {
                    dashboardScheduleAdapter.setSchedules(schedules)
                    dashboardScheduleAdapter.notifyDataSetChanged()
                    lyt_empty_upcoming_schedule.visible()
                }
            })

        with(rv_upcoming_schedule) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = dashboardScheduleAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        getUpcomingSchedule()
    }
}