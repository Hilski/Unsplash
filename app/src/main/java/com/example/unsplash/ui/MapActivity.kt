package com.example.unsplash.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.unsplash.R
import com.example.unsplash.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapBinding
    private var map: GoogleMap? = null
    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private var needAnimateCamera = false
    private var needMoveCamera = true
    private var handler = Handler(Looper.getMainLooper())
    private var cameraMovedRunnable = Runnable {
        needMoveCamera = true
    }

    private val viewModel: MapViewModel by viewModels()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {
            startLocation()
        }
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                locationListener?.onLocationChanged(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        map?.isMyLocationEnabled = true
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1_000).build()
        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapOverlay.setOnTouchListener { v, event ->
            handler.removeCallbacks(cameraMovedRunnable)
            needMoveCamera = false
            handler.postDelayed(cameraMovedRunnable, 5_000)
            false
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap

            checkPermissions()

            createMarker()

            with(googleMap.uiSettings) {
                this.isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
            googleMap.setLocationSource(object : LocationSource {
                override fun activate(p0: LocationSource.OnLocationChangedListener) {
                    locationListener = p0
                }

                override fun deactivate() {
                    locationListener = null
                }
            })
        }

        fusedClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun createMarker() {
        lifecycleScope.launchWhenStarted {
            viewModel.markersFlow.collect {
                for (i in 0..3) {
                    map?.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                viewModel.markersFlow.value[i][0] as Double,
                                viewModel.markersFlow.value[i][1] as Double
                            )
                        )
                            .title(viewModel.markersFlow.value[i][2].toString())
                    )
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates(locationCallback)
        needAnimateCamera = true
    }

    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startLocation()
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}