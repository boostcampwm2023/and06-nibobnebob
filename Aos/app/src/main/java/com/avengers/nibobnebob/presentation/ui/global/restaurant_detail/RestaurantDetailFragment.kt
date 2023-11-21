package com.avengers.nibobnebob.presentation.ui.global.restaurant_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class RestaurantDetailFragment : BaseFragment<FragmentRestaurantDetailBinding>(R.layout.fragment_restaurant_detail) {
    override val parentViewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}