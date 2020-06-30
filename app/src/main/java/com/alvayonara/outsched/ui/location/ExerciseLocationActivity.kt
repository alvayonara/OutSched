package com.alvayonara.outsched.ui.location

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.alvayonara.outsched.R
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_ADDRESS
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_LATITUDE
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_LONGITUDE
import com.alvayonara.outsched.utils.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.alvayonara.outsched.utils.PermissionUtils.isPermissionGranted
import com.alvayonara.outsched.utils.PermissionUtils.requestPermission
import com.alvayonara.outsched.utils.ToolbarConfig
import com.alvayonara.outsched.utils.invisible
import com.alvayonara.outsched.utils.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_exercise_location.*
import java.io.IOException
import java.util.*

class ExerciseLocationActivity : AppCompatActivity(), OnMapReadyCallback,
    OnRequestPermissionsResultCallback {

    private var permissionDenied = false
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var id: Int? = 0

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_location)

        initToolbar()

        // Intent from change schedule (id)
        id = intent.extras?.getInt(EXTRA_ID, 0)
        Log.d("id_ex", id.toString())

        // Initialize map fragment
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Set Exercise Location"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ToolbarConfig.setSystemBarColor(this, android.R.color.white)
        ToolbarConfig.setSystemBarLight(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()
        initCameraIdle()
    }

    private fun initViewMyLocation() {
        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )

        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 600)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            initViewMyLocation()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng =
                            LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
                    }
                }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        // [END maps_check_location_permission]
    }

    private fun initCameraIdle() {
        mMap.setOnCameraIdleListener {
            // Get center latitude and longitude
            val center = mMap.cameraPosition.target as LatLng

            // Geocoder using AsyncTask
            AsyncTask.execute {
                run {
                    // Pass data center of latitude and longitude to get address using geocode class
                    val address = getAddressFromLatLong(center)

                    runOnUiThread {
                        // Init view in UI thread
                        initViewAddress(address, center)
                    }
                }
            }
        }
    }

    private fun initViewAddress(address: String?, center: LatLng) {
        if (address != null) {
            tv_address.text = address
            address_card_view.visible()
            btn_save_location.setOnClickListener {
                val builder = AlertDialog.Builder(this@ExerciseLocationActivity)

                builder.setTitle("Confirmation")
                builder.setMessage("Do you want to set this location?")
                builder.setPositiveButton("Yes") { _, _ ->
                    run {
                        val intent = Intent(this, SelectScheduleActivity::class.java).apply {
                            putExtra(EXTRA_ADDRESS, address)
                            putExtra(EXTRA_LATITUDE, center.latitude.toString())
                            putExtra(EXTRA_LONGITUDE, center.longitude.toString())

                            putExtra(EXTRA_ID, id)
                        }
                        startActivity(intent)
                    }
                }

                builder.setNegativeButton("No") { dialog: DialogInterface, _ ->
                    dialog.cancel()
                }

                builder.show()
            }
        } else {
            // When address return null
            address_card_view.invisible()
        }
    }

    private fun getAddressFromLatLong(center: LatLng): String? {
        val geocode = Geocoder(this, Locale.getDefault())
        val address: String?

        address = try {
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

        return address
    }

    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()

            // Init idle map camera if the permission has been granted.
            initCameraIdle()
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
            // [END_EXCLUDE]
        }
    }

    // [END maps_check_location_permission_result]
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
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