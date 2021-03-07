package com.alvayonara.outsched.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.MyApplication
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.ui.DashboardScheduleAdapter
import com.alvayonara.outsched.core.ui.ViewModelFactory
import com.alvayonara.outsched.core.utils.gone
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.FragmentScheduleBinding
import com.alvayonara.outsched.ui.base.BaseFragment
import javax.inject.Inject

class PastScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

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

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleBinding
        get() = FragmentScheduleBinding::inflate

    override fun setup() {
        if (activity != null) {
            initView()
            getPastSchedule()
        }
    }

    private fun initView() {
        dashboardScheduleAdapter = DashboardScheduleAdapter()
        with(binding.rvScheduleList) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = dashboardScheduleAdapter
        }
    }

    private fun getPastSchedule() {
        binding.progressBar.visible()
        dashboardViewModel.getPastSchedules
            .observe(viewLifecycleOwner, {
                binding.progressBar.gone()
                dashboardScheduleAdapter.setSchedules(it)
                binding.viewEmptySchedule.tvEmptyScheduleTitle.text =
                    getString(R.string.empty_past_schedule_title)
                binding.viewEmptySchedule.tvEmptyScheduleDescription.text =
                    getString(R.string.empty_past_schedule_description)
                binding.viewEmptySchedule.root.visibility =
                    if (it.isNotEmpty()) View.GONE else View.VISIBLE
            })
    }

    override fun onResume() {
        super.onResume()
        getPastSchedule()
    }
}