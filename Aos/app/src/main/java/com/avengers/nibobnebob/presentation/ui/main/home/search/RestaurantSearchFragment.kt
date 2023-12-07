package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.adjustKeyboard
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.adapter.HomeSearchAdapter
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RestaurantSearchFragment :
    BaseFragment<FragmentRestaurantSearchBinding>(R.layout.fragment_restaurant_search) {
    private val viewModel: RestaurantSearchViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()

    private val adapter = HomeSearchAdapter { item ->
        viewModel.onClickSearchItem(item)
    }

    override fun initView() {
        binding.vm = viewModel
        binding.rvSearch.adapter = adapter
        setFocus()
        fetchCurrentLocation()
        view?.let { clearFocus(it) }
        initStateObserver()
    }

    override fun initNetworkView() {
        //TODO : 네트워크
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is RestaurantSearchEvent.OnClickResultItem -> {
                        findNavController().toSearchMap()
                        parentViewModel.markSearchRestaurant(it.item)
                    }

                    is RestaurantSearchEvent.NavigateToHome -> findNavController().popBackStack()
                }

            }
        }
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collectLatest {
                adapter.setResultList(it.searchList, it.searchKeyword)
            }
        }
    }

    private fun checkLocationPermission() : Boolean{
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED &&
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        )
    }

    private fun fetchCurrentLocation() {

        if(checkLocationPermission()){
            LocationServices.getFusedLocationProviderClient(activity as MainActivity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    viewModel.setCurrentLocation(location?.latitude, location?.longitude)
                }
            }
        } else {
            showToastMessage("GPS나 위치 권한을 허용해주세요.")
        }
    }

    private fun NavController.toSearchMap() {
        val action =
            RestaurantSearchFragmentDirections.actionRestaurantSearchFragmentToRestaurantSearchMapFragment()
        navigate(action)
    }

    private fun setFocus() = with(binding) {
        tietInputSearch.requestFocus()

        repeatOnStarted {
            parentViewModel.searchKeyword.collectLatest {
                tietInputSearch.setText(it)
                tietInputSearch.setSelection(it.length)

                if (it.isNotEmpty()) {
                    viewModel.searchRestaurant(it)
                }
            }
        }

        requireActivity().adjustKeyboard(tietInputSearch.findFocus(), true)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFocus(view: View) {
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.tietInputSearch.clearFocus()
                requireContext().adjustKeyboard(view, false)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentViewModel.clearKeyword()
    }
}