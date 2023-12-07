package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantSearchMapBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.RestaurantBottomSheet
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.avengers.nibobnebob.presentation.ui.toAddRestaurant
import com.avengers.nibobnebob.presentation.ui.toHome
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RestaurantSearchMapFragment :
    BaseFragment<FragmentRestaurantSearchMapBinding>(R.layout.fragment_restaurant_search_map),
    OnMapReadyCallback {
    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RestaurantSearchMapViewModel by viewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var isWish = false


    private val locationPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is SearchMapEvents.ShowSnackMessage -> showSnackBar(it.msg)
                }
            }
        }
    }

    override fun initView() {
        initMapView()
        initClickEvent()
    }

    override fun initNetworkView() {}

    private fun initStateObserver() {
        repeatOnStarted {
            parentViewModel.selectedItem.collectLatest {
                if (it.id < 0) return@collectLatest

                binding.tvSearchKeyword.text = it.name
                isWish = it.isInWishList

                viewModel.getRestaurantIsWish(it.id, it.isInWishList)
                viewModel.wishChanged.collectLatest { changed ->
                    if (changed == WishStatus.INIT) return@collectLatest

                    val data = it.copy(isInWishList = !it.isInWishList)
                    if (changed == WishStatus.CHANGED) isWish = !it.isInWishList
                    setSearchResultMarker(if (changed == WishStatus.CHANGED) data else it)
                    RestaurantBottomSheet(
                        context = requireContext(),
                        data = if (changed == WishStatus.CHANGED) data else it,
                        onClickAddWishRestaurant = ::addWishTest,
                        onClickAddMyRestaurant = ::addRestaurantTest,
                        onClickGoReview = ::goReviewTest
                    ).show()
                }

            }
        }
    }

    private fun initMapView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.search_map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
                }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun initClickEvent() = with(binding) {
        ivClose.setOnClickListener { findNavController().toHome() }
        listOf(ibBack, vSearch).forEach {
            it.setOnClickListener {
                parentViewModel.keepSearchKeyword(binding.tvSearchKeyword.text.toString())
                findNavController().popBackStack()
            }
        }
    }


    private fun setSearchResultMarker(data: UiRestaurantData) {
        val marker = Marker().apply {
            position = LatLng(data.latitude, data.longitude)
            icon = OverlayImage.fromResource(R.drawable.ic_marker)
            map = naverMap
        }

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(data.latitude, data.longitude))
        naverMap.moveCamera(cameraUpdate)


        marker.setOnClickListener {
            RestaurantBottomSheet(
                context = requireContext(),
                data = data.copy(isInWishList = isWish),
                onClickAddWishRestaurant = ::addWishTest,
                onClickAddMyRestaurant = ::addRestaurantTest,
                onClickGoReview = ::goReviewTest
            ).show()
            true
        }
    }

    override fun onMapReady(nM: NaverMap) {
        this.naverMap = nM
        with(naverMap.uiSettings) {
            isCompassEnabled = false
            isZoomControlEnabled = false
        }

        naverMap.locationSource = locationSource
        initStateObserver()
    }

    private suspend fun addWishTest(id: Int, curState: Boolean): Boolean {
        return lifecycleScope.async {
            isWish = !curState
            viewModel.updateWish(id, curState)
        }.await()
    }

    private fun addRestaurantTest(restaurantName: String, restaurantId: Int) {
        findNavController().toAddRestaurant(restaurantName, restaurantId)
    }

    private fun goReviewTest(restaurantId: Int) {
        findNavController().toRestaurantDetail(restaurantId)
    }

    private fun checkPermissions() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                // todo
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    locationPermissionList,
                    1000
                )
            }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


    private fun NavController.toRestaurantDetail(restaurantId: Int) {
        val action =
            RestaurantSearchMapFragmentDirections.actionRestaurantSearchMapFragmentToRestaurantDetailFragment(
                restaurantId
            )
        navigate(action)
    }

}