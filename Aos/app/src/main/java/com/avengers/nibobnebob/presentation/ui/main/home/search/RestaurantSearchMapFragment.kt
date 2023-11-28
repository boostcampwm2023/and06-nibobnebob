package com.avengers.nibobnebob.presentation.ui.main.home.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantSearchMapBinding
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class RestaurantSearchMapFragment : BaseFragment<FragmentRestaurantSearchMapBinding>(R.layout.fragment_restaurant_search_map) {
    override val parentViewModel: MainViewModel by activityViewModels()


}