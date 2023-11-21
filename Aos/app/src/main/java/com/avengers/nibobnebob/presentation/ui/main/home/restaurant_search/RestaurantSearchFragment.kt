package com.avengers.nibobnebob.presentation.ui.main.home.restaurant_search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class RestaurantSearchFragment : BaseFragment<FragmentRestaurantSearchBinding>(R.layout.fragment_restaurant_search) {
    override val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}