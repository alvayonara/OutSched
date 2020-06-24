package com.alvayonara.outsched.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity
import kotlinx.android.synthetic.main.activity_exercise_location.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.toolbar

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initView()
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {

        }
    }

    private fun initView() {
        // init ViewPager TabLayout
        val sectionPageAdapter =
            DashboardSectionPagerAdapter(
                requireActivity(),
                childFragmentManager
            )
        view_pager_dashboard.adapter = sectionPageAdapter
        tabs_dashboard.setupWithViewPager(view_pager_dashboard)

        fab_add_schedule.setOnClickListener {
            val intent = Intent(requireActivity(), ExerciseLocationActivity::class.java)
            startActivity(intent)
        }
    }
}