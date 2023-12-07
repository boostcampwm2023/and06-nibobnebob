package com.avengers.nibobnebob.presentation.ui.main.home

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentHomeBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.RestaurantBottomSheet
import com.avengers.nibobnebob.presentation.ui.checkLocationIsOn
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.adapter.HomeFilterAdapter
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.avengers.nibobnebob.presentation.ui.requestLocationPermission
import com.avengers.nibobnebob.presentation.ui.toAddRestaurant
import com.avengers.nibobnebob.presentation.ui.toRestaurantDetail
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private val viewModel: HomeViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()
    private val args: HomeFragmentArgs by navArgs()
    private val restaurantId by lazy { args.addRestaurantId }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val markerList = mutableListOf<Marker>()

    private val locationPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun initView() {
        binding.vm = viewModel
        initMapView()
        binding.rvHomeFilter.adapter = HomeFilterAdapter()
        viewModel.setAddRestaurantId(restaurantId)
    }

    override fun initNetworkView() {
        viewModel.getFilterList()
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

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is HomeEvents.NavigateToSearchRestaurant -> findNavController().toSearchRestaurant()
                    is HomeEvents.SetNewMarkers -> {
                        handleMarkersEvent()
                    }

                    is HomeEvents.SetSingleMarker -> {
                        setSingleMarker(event.marker, event.item)
                    }

                    is HomeEvents.RemoveMarkers -> removeAllMarker()
                    is HomeEvents.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }
    }

    private fun handleMarkersEvent() {
        removeAllMarker()
        viewModel.trackingOff()
        val lat = viewModel.uiState.value.cameraLatitude
        val lng = viewModel.uiState.value.cameraLongitude
        val zoom = viewModel.uiState.value.cameraZoom

        val cameraPosition = CameraPosition(LatLng(lat, lng), zoom)
        val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition)
            .apply { animate(CameraAnimation.Fly, 500) }

        naverMap.moveCamera(cameraUpdate)

        viewModel.uiState.value.markerList.forEach { data ->
            setMarker(data)
        }
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
    }

    private fun setMapListener() {

        // todo 화면 이동시 리스너
        naverMap.addOnCameraChangeListener { _, _ ->

        }

        naverMap.addOnCameraIdleListener {
            val cameraPosition = naverMap.cameraPosition
            viewModel.updateCamera(
                cameraPosition.target.latitude,
                cameraPosition.target.longitude,
                cameraPosition.zoom
            )
        }

        naverMap.addOnLocationChangeListener {
            viewModel.updateLocation(
                it.latitude,
                it.longitude
            )
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

    private fun setMarker(data: UiRestaurantData) {
        val marker = Marker()

        marker.position = LatLng(data.latitude, data.longitude)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
        marker.map = naverMap

        marker.setOnClickListener {
            val bottomSheet = RestaurantBottomSheet(
                context = requireContext(),
                data = data,
                onClickAddWishRestaurant = ::addWishTest,
                onClickAddMyRestaurant = ::addRestaurantTest,
                onClickGoReview = ::goReviewTest
            )
            viewModel.setSelectedMarker(marker)
            bottomSheet.show()

            true
        }
        markerList.add(marker)
    }

    private fun setSingleMarker(marker: Marker?, item: UiRestaurantData) {

        marker?.setOnClickListener {
            val bottomSheet = RestaurantBottomSheet(
                context = requireContext(),
                data = item,
                onClickAddWishRestaurant = ::addWishTest,
                onClickAddMyRestaurant = ::addRestaurantTest,
                onClickGoReview = ::goReviewTest
            )
            bottomSheet.show()

            true
        }

    }


    // todo 모든 marker 데이터 markerList 에 저장해 놨다가, remove 다음 방식으로 진행
    private fun removeAllMarker() {
        markerList.forEach {
            it.map = null
        }
        markerList.clear()
    }


    private suspend fun addWishTest(id: Int, curState: Boolean): Boolean {
        return lifecycleScope.async {
            viewModel.updateWish(id, curState)
        }.await()
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



