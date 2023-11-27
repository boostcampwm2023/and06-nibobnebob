package com.avengers.nibobnebob.presentation.ui.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.app.App
import com.avengers.nibobnebob.databinding.FragmentHomeBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.RestaurantBottomSheet
import com.avengers.nibobnebob.presentation.ui.checkLocationIsOn
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.adapter.HomeFilterAdapter
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.avengers.nibobnebob.presentation.ui.requestLocationPermission
import com.avengers.nibobnebob.presentation.ui.toAddRestaurant
import com.avengers.nibobnebob.presentation.ui.toRestaurantDetail
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val markerList = mutableListOf<Marker>()

    private val locationPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initMapView()
        initEventObserver()
        binding.rvHomeFilter.adapter = HomeFilterAdapter()
        viewModel.getFilterList()
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
        viewModel.getMarkerList()
        setLocation()
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

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is HomeEvents.NavigateToSearchRestaurant -> findNavController().toSearchRestaurant()
                    is HomeEvents.SetNewMarkers -> {
                        viewModel.uiState.value.markerList.forEach { data ->
                            setMarker(data)
                        }
                    }
                    is HomeEvents.RemoveMarkers -> removeAllMarker()
                }
            }
        }
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.locationTrackingState) {
                    is TrackingState.TryOn -> {
                        requireContext().requestLocationPermission(
                            locationPermissionList,
                            ::startPermissionLauncher,
                            ::onTrackingChangeListener
                        )
                    }

                    is TrackingState.On -> {
                        naverMap.locationTrackingMode =
                            LocationTrackingMode.Follow
                    }
                    is TrackingState.Off -> naverMap.locationTrackingMode =
                        LocationTrackingMode.None
                }
            }
        }
    }

    private fun setLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        if(ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if(location!= null){
                    viewModel.updateLocation(
                        location.latitude,
                        location.longitude
                    )
                }
            }
        }
    }

    private fun startPermissionLauncher() {
        requestPermissionLauncher.launch(locationPermissionList)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val isAllGranted = locationPermissionList.all { resultMap[it] == true }
        if (isAllGranted) requireContext().checkLocationIsOn(::onTrackingChangeListener)
        else viewModel.trackingOff()
    }

    private fun onTrackingChangeListener(state: Boolean) {
        if (state) viewModel.trackingOn()
        else viewModel.trackingOff()
    }

    // todo markerData model을 정의하여, 파라미터로 해당 데이터를 삽입
    private fun setMarker(data: UiRestaurantData) {
        val marker = Marker()

        marker.position = LatLng(data.latitude, data.longitude)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_location_circle)
        marker.map = naverMap

        marker.setOnClickListener {
            val bottomSheet = RestaurantBottomSheet(
                context = requireContext(),
                data = data,
                onClickAddWishRestaurant = ::addWishTest,
                onClickAddMyRestaurant = ::addRestaurantTest,
                onClickGoReview = ::goReviewTest
            )
            bottomSheet.show()

            true
        }
        markerList.add(marker)
    }

    // todo 모든 marker 데이터 markerList 에 저장해 놨다가, remove 다음 방식으로 진행
    private fun removeAllMarker() {
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
    }


    private fun addWishTest(id: Int, curState: Boolean): Boolean {
        // todo wish 맛집 리스트 에 추가 or 삭제 API 통신
        return true
    }

    private fun addRestaurantTest(restaurantName: String, restaurantId: Int) {
        findNavController().toAddRestaurant(restaurantName, restaurantId)
    }

    private fun goReviewTest(restaurantId: Int) {
        findNavController().toRestaurantDetail(restaurantId)
    }


    private fun NavController.toSearchRestaurant() {
        val action = HomeFragmentDirections.actionHomeFragmentToRestaurantSearchFragment()
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

