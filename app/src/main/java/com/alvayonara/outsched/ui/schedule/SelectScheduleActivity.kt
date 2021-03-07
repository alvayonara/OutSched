package com.alvayonara.outsched.ui.schedule

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.outsched.MyApplication
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import com.alvayonara.outsched.core.ui.SelectScheduleAdapter
import com.alvayonara.outsched.core.ui.ViewModelFactory
import com.alvayonara.outsched.core.utils.Helper.showMaterialDialog
import com.alvayonara.outsched.core.utils.gone
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.ActivitySelectScheduleBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import com.alvayonara.outsched.ui.location.ExerciseLocationActivity.Companion.EXTRA_ID
import kotlinx.android.synthetic.main.activity_select_schedule.*
import javax.inject.Inject


class SelectScheduleActivity : BaseActivity<ActivitySelectScheduleBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val scheduleViewModel: SelectScheduleViewModel by viewModels {
        factory
    }

    private lateinit var selectScheduleAdapter: SelectScheduleAdapter

    private var address: String = ""
    private var latitude: String = ""
    private var longitude: String = ""
    private var id: Int = 0
    private var requestCode: Int = 0

    companion object {
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_REQUEST_CODE = "extra_request_code"
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySelectScheduleBinding
        get() = ActivitySelectScheduleBinding::inflate

    override fun setup() {
        (application as MyApplication).appComponent.inject(this)
        initToolbar()
        initData()
        initView()
        getSchedulesData()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = "Select Schedule"
    }

    private fun initData() {
        intent.extras?.let {
            address = it.getString(EXTRA_ADDRESS).orEmpty()
            latitude = it.getString(EXTRA_LATITUDE).orEmpty()
            longitude = it.getString(EXTRA_LONGITUDE).orEmpty()
            id = it.getInt(EXTRA_ID, 0)
            requestCode = it.getInt(EXTRA_REQUEST_CODE, 0)
        }
    }

    private fun initView() {
        selectScheduleAdapter = SelectScheduleAdapter()
        with(binding.rvSchedule) {
            layoutManager = LinearLayoutManager(this@SelectScheduleActivity)
            adapter = selectScheduleAdapter
        }
    }

    private fun getSchedulesData() {
        binding.progressBar.visible()
        scheduleViewModel.getWeathers(latitude, longitude, address, id, requestCode).observe(this, {
            if (it != null) {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.progressBar.gone()
                        selectScheduleAdapter.setSchedules(it.data)
                    }
                    is ApiResponse.Empty -> {
                        binding.progressBar.gone()
                    }
                    is ApiResponse.Error -> {
                        binding.progressBar.gone()
                    }
                }
            }
        })
    }

    private fun backAlertDialog() {
        showMaterialDialog(
            context = this,
            title = R.string.dialog_confirmation,
            message = R.string.change_location_dialog,
            positiveText = R.string.dialog_yes,
            negativeText = R.string.dialog_no,
            actionPositive = {
                finish()
            },
            actionNegative = { }
        )
    }

    override fun onBackPressed() = backAlertDialog()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                backAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        getSchedulesData()
    }
}