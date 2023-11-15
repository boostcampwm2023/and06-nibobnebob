package com.avengers.nibobnebob.presentation.ui.main.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentHomeBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private val viewModel: HomeViewModel by viewModels()

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val locationPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initMapView()
    }

    private fun initMapView(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapFragment.getMapAsync(this)
    }

    // NaverMap 관련 Setting
    override fun onMapReady(nM: NaverMap) {
        this.naverMap = nM
        // 맵 ui Settings
        with(naverMap.uiSettings) {
            isCompassEnabled = false
            isZoomControlEnabled = false
        }

        naverMap.locationSource = locationSource
        initStateObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when(it.locationTrackingState){
                    is TrackingState.TryOn -> requestLocationPermission()
                    is TrackingState.On -> naverMap.locationTrackingMode = LocationTrackingMode.Follow
                    is TrackingState.Off -> naverMap.locationTrackingMode = LocationTrackingMode.None
                }
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionList.forEach { permission ->
            requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) checkLocationIsOn()
        else viewModel.trackingOff()
    }

    private fun checkLocationIsOn() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            viewModel.trackingOn()
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            viewModel.trackingOff()
        }
    }
}

