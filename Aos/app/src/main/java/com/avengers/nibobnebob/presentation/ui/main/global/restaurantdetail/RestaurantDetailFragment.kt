package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail.adapter.RestaurantReviewAdapter
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment :
    BaseFragment<FragmentRestaurantDetailBinding>(R.layout.fragment_restaurant_detail) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RestaurantDetailViewModel by viewModels()

    val args: RestaurantDetailFragmentArgs by navArgs()
    private val restaurantId by lazy { args.restaurantId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setRestaurantId(restaurantId)
        viewModel.restaurantDetail()
        initEventObserver()
        binding.rvReview.adapter = RestaurantReviewAdapter()

    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is RestaurantDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is RestaurantDetailEvents.NavigateToDetailReview -> {
                        // 상세리뷰로 이동
                    }
                    is RestaurantDetailEvents.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }
    }

}