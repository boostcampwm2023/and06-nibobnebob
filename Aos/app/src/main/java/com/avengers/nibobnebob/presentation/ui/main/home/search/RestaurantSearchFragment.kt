package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
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
import com.avengers.nibobnebob.presentation.ui.toHome
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectEvent()
        setFocus()
        clearFocus(view)
        fetchCurrentLocation()

    }

    private fun initView() {
        binding.vm = viewModel
        binding.rvSearch.adapter = adapter

        repeatOnStarted {
            viewModel.uiState.collectLatest {
                adapter.setResultList(it.searchList, it.searchKeyword)
            }
        }

    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is RestaurantSearchEvent.OnClickResultItem -> {
                        findNavController().toSearchMap()
                        parentViewModel.markSearchRestaurant(it.item)
                    }

                    is RestaurantSearchEvent.NavigateToHome -> findNavController().toHome()
                    else -> {}
                }

            }
        }
    }

    private fun fetchCurrentLocation() {
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
                LocationServices.getFusedLocationProviderClient(activity as MainActivity).apply {
                    lastLocation.addOnSuccessListener { location: Location? ->
                        viewModel.setCurrentLocation(location?.latitude, location?.longitude)
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1000
                )
            }
        }
    }

    private fun NavController.toSearchMap() {
        val action =
            RestaurantSearchFragmentDirections.actionRestaurantSearchFragmentToRestaurantSearchMapFragment()
        navigate(action)
    }

    private fun setFocus() {
        binding.tietInputSearch.requestFocus()
        requireActivity().adjustKeyboard(binding.tietInputSearch.findFocus(), true)
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
}