package com.avengers.nibobnebob.presentation.ui.global.restaurant_add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentAddMyRestaurantBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class AddMyRestaurantFragment : BaseFragment<FragmentAddMyRestaurantBinding>(R.layout.fragment_add_my_restaurant) {

    override val parentViewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}