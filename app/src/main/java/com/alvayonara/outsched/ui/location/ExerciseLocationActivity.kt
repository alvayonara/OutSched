package com.alvayonara.outsched.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.utils.Helper.showMaterialDialog
import com.alvayonara.outsched.core.utils.ToolbarConfig
import com.alvayonara.outsched.core.utils.invisible
import com.alvayonara.outsched.core.utils.visible
import com.alvayonara.outsched.databinding.ActivityExerciseLocationBinding
import com.alvayonara.outsched.ui.base.BaseActivity
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_ADDRESS
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_LATITUDE
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_LONGITUDE
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_REQUEST_CODE
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_exercise_location.*
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors


class ExerciseLocationActivity : BaseActivity<ActivityExerciseLocationBinding>(),
    OnMapReadyCallback,
    OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var id = 0
    private var requestCode = 0

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override val bindingInflater: (LayoutInflater) -> ActivityExerciseLocationBinding
        get() = ActivityExerciseLocationBinding::inflate

    override fun setup() {
        initToolbar()
        initData()
        initMapLocation()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Set Exercise Location"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ToolbarConfig.setSystemBarColor(this, android.R.color.white)
        ToolbarConfig.setSystemBarLight(this)
    }

    private fun initData() {
        // Intent from change schedule (id & request code)
        intent.extras?.let {
            id = it.getInt(EXTRA_ID, 0)
            requestCode = it.getInt(EXTRA_REQUEST_CODE, 0)
        }
    }

    private fun initMapLocation() {
        // Initialize map fragment
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        requestPermission()
    }

    private fun requestPermission() {
        requestPermissions(listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
            action = {
                enableMyLocation()
            }, actionDeny = { showDenyPermission() })
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        mMap.isMyLocationEnabled = true
        initViewMyLocation()

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng =
                        LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
                }
            }

        initCameraIdle()
    }

    private fun initViewMyLocation() {
        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )

        (locationButton.layoutParams as RelativeLayout.LayoutParams).apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            setMargins(0, 0, 30, 600)
        }
    }

    private fun initCameraIdle() {
        mMap.setOnCameraIdleListener {
            // Get center latitude and longitude
            val center = mMap.cameraPosition.target as LatLng
            Executors.newSingleThreadExecutor().execute {
                val mainHandler = Handler(Looper.getMainLooper())

                // Pass data center of latitude and longitude to get address using geocode class
                val address = getAddressFromLatLong(center)

                //sync calculations
                mainHandler.post {
                    // Init view in UI thread
                    initViewAddress(address, center)
                }
            }
        }
    }

    private fun initViewAddress(address: String?, center: LatLng) {
        if (address != null) {
            binding.tvAddress.text = address
            binding.addressCardView.visible()
            binding.btnSaveLocation.setOnClickListener {
                showMaterialDialog(
                    context = this,
                    title = R.string.dialog_confirmation,
                    message = R.string.location_dialog,
                    positiveText = R.string.dialog_yes,
                    negativeText = R.string.dialog_no,
                    actionPositive = {
                        val intent = Intent(this, SelectScheduleActivity::class.java).apply {
                            putExtra(EXTRA_ADDRESS, address)
                            putExtra(EXTRA_LATITUDE, center.latitude.toString())
                            putExtra(EXTRA_LONGITUDE, center.longitude.toString())
                            putExtra(EXTRA_ID, id)
                            putExtra(EXTRA_REQUEST_CODE, requestCode)
                        }
                        startActivity(intent)
                    },
                    actionNegative = { }
                )
            }
        } else {
            // When address return null
            binding.addressCardView.invisible()
        }
    }

    private fun getAddressFromLatLong(center: LatLng): String? {
        val geocode = Geocoder(this, Locale.getDefault())
        return try {
            val addressList =
                geocode.getFromLocation(center.latitude, center.longitude, 1)
            if (addressList != null && addressList.size > 0) {
                addressList[0].getAddressLine(0)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}