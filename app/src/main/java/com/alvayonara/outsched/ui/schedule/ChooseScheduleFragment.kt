package com.alvayonara.outsched.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alvayonara.outsched.R

class ChooseScheduleFragment : Fragment() {

    private lateinit var address: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    companion object {
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_choose_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            address = arguments?.getString(EXTRA_ADDRESS)!!
            latitude = arguments?.getString(EXTRA_LATITUDE)!!
            longitude = arguments?.getString(EXTRA_LONGITUDE)!!
        }
    }
}