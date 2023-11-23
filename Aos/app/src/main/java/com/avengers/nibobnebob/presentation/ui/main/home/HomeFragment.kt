package com.avengers.nibobnebob.presentation.ui.main.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.NavGraphDirections
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentHomeBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.util.restaurantSheet
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private val viewModel: HomeViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()

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
        binding.ivTest.setOnClickListener {
            findNavController().toSearchRestaurant()
        }
        initMapView()
    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
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
        setMapListener()
        initStateObserver()
        setMarker()
    }

    private fun setMapListener() {

        // todo 화면 이동시 리스너
        naverMap.addOnCameraChangeListener { reason, animated ->

        }

        // todo 화면이동 끝났을때 리스너
        naverMap.addOnCameraIdleListener {

        }

        // todo GPS 기반 위치변화 리스너
        naverMap.addOnLocationChangeListener {

        }
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.locationTrackingState) {
                    is TrackingState.TryOn -> requestLocationPermission()
                    is TrackingState.On -> naverMap.locationTrackingMode =
                        LocationTrackingMode.Follow

                    is TrackingState.Off -> naverMap.locationTrackingMode =
                        LocationTrackingMode.None
                }
            }
        }
    }

    private fun requestLocationPermission() {
        var permissionFlag = false
        locationPermissionList.forEach { permission ->
            permissionFlag = ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionFlag) {
            checkLocationIsOn()
        } else {
            requestPermissionLauncher.launch(locationPermissionList)
            Toast.makeText(requireContext(), "위치권한을 허용해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val isAllGranted = locationPermissionList.all { resultMap[it] == true }
        if (isAllGranted) checkLocationIsOn()
        else viewModel.trackingOff()
    }

    private fun checkLocationIsOn() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            viewModel.trackingOn()
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            Toast.makeText(requireContext(), "휴대폰 GPS를 켜주세요", Toast.LENGTH_SHORT).show()
            viewModel.trackingOff()
        }
    }

    // todo markerData model을 정의하여, 파라미터로 해당 데이터를 삽입
    private fun setMarker() {
        val marker = Marker()

        // example
        marker.position = LatLng(37.555594049034, 126.96707115682)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_location_circle)
        marker.map = naverMap

        marker.setOnClickListener {
            restaurantSheet(
                context = requireContext(),
                restaurantId = 0,
                isWish = false,
                onClickAddWishRestaurant = ::addWishTest,
                onClickAddMyRestaurant = ::addRestaurantTest,
                onClickGoReview = ::goReviewTest
            ).show()

            true
        }

        // todo 마커의 포지션을 정함
        // marker.position = LatLng(data.latitude,data.longitude)

        // todo 마커의 icon을 정함
        // marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)

        // todo 마커 클릭 이벤트를 등록함
        // marker.setOnClickListener{}

        // todo 마커를 map에 찍음
        // marker.map = naverMap
    }

    private fun addWishTest(test: Int, test2: Boolean): Boolean {
        return true
    }

    private fun addRestaurantTest(test: Int) {
        findNavController().toAddRestaurant()
    }

    private fun goReviewTest(test: Int) {
        findNavController().toRestaurantDetail()
    }

    private fun NavController.toAddRestaurant() {
        val action = NavGraphDirections.globalToAddMyRestaurantFragment()
        navigate(action)
    }

    private fun NavController.toSearchRestaurant() {
        val action = HomeFragmentDirections.actionHomeFragmentToRestaurantSearchFragment()
        navigate(action)
    }

    private fun NavController.toRestaurantDetail() {
        val action = NavGraphDirections.globalToRestaurantDetailFragment()
        navigate(action)
    }


}


@BindingAdapter("trackingBtnDrawable")
fun bindTrackingBtnDrawable(btn: ImageButton, state: TrackingState) {
    when (state) {
        is TrackingState.On -> btn.setImageResource(R.drawable.ic_location_on)
        is TrackingState.Off -> btn.setImageResource(R.drawable.ic_location_off)
        else -> {}
    }
}

