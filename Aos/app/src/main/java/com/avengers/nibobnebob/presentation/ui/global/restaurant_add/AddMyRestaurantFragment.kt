package com.avengers.nibobnebob.presentation.ui.global.restaurant_add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentAddMyRestaurantBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMyRestaurantFragment : BaseFragment<FragmentAddMyRestaurantBinding>(R.layout.fragment_add_my_restaurant) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: AddMyRestaurantViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setSliderListener()
        setVisitMethodRadioListener()
    }

    private fun setSliderListener(){
        with(binding){
            sdParkingSpace.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.PARKING, value.toInt())
            }

            sdService.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.SERVICE, value.toInt())
            }

            sdTaste.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.TASTE, value.toInt())
            }

            sdToilet.addOnChangeListener { _, value, _ ->
                viewModel.sliderStateChange(EstimateItem.TOILET, value.toInt())
            }
        }
    }

    private fun setVisitMethodRadioListener(){
        binding.rgVisitMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_visit_not_car -> viewModel.setIsVisitWithCar(false)
                R.id.rb_visit_car -> viewModel.setIsVisitWithCar(true)
            }
        }

    }
}