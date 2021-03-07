package com.alvayonara.outsched.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.MyApplication
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.ui.DashboardScheduleAdapter
import com.alvayonara.outsched.core.ui.ViewModelFactory
import com.alvayonara.outsched.core.utils.gone
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.FragmentPastScheduleBinding
import com.alvayonara.outsched.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_past_schedule.*
import javax.inject.Inject

class PastScheduleFragment : BaseFragment<FragmentPastScheduleBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val dashboardViewModel: DashboardViewModel by viewModels {
        factory
    }

    private lateinit var dashboardScheduleAdapter: DashboardScheduleAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPastScheduleBinding
        get() = FragmentPastScheduleBinding::inflate

    override fun setup() {
        if (activity != null){
            initView()
            getPastSchedule()
        }
    }

    private fun initView() {
        dashboardScheduleAdapter = DashboardScheduleAdapter()
        with(binding.rvPastSchedule) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = dashboardScheduleAdapter
        }
    }

    private fun getPastSchedule() {
        binding.progressBarPastSchedule.visible()
        dashboardViewModel.getPastSchedules
            .observe(viewLifecycleOwner, { schedules ->
                binding.progressBarPastSchedule.gone()

                if (schedules.isNotEmpty()) {
                    dashboardScheduleAdapter.setSchedules(schedules)
                } else {
                    dashboardScheduleAdapter.setSchedules(schedules)
                    binding.lytEmptyPastSchedule.visible()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getPastSchedule()
    }
}