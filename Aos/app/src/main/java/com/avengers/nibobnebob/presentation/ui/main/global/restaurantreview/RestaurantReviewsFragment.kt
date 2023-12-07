package com.avengers.nibobnebob.presentation.ui.main.global.restaurantreview

import android.util.Log
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantReviewsBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.util.Constants

class RestaurantReviewsFragment :
    BaseFragment<FragmentRestaurantReviewsBinding>(R.layout.fragment_restaurant_reviews) {
    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RestaurantReviewsViewModel by viewModels()

    private val args: RestaurantReviewsFragmentArgs by navArgs()
    private val restaurantId by lazy { args.restaurantId }
    private val restaurantName by lazy { args.restaurantName }

    override fun initView() {
        setFilterMenu()
        Log.d("TEST", "$restaurantId, $restaurantName")
    }

    override fun initEventObserver() {
    }

    override fun initNetworkView() {
    }

    private fun setFilterMenu() {

        binding.tvFilter.setOnClickListener {
            PopupMenu(requireContext(), binding.ivFilter).apply {
                menuInflater.inflate(R.menu.review_filter_menu, menu)
                setOnMenuItemClickListener {
                    viewModel.sortReview(
                        sort =
                        when (it.itemId) {
                            R.id.menu_new -> Constants.FILTER_NEW
                            R.id.menu_old -> Constants.FILTER_OLD
                            R.id.menu_best -> Constants.FILTER_BEST
                            R.id.menu_worst -> Constants.FILTER_WORST
                            else -> null
                        }
                    )

                    true
                }
                show()
            }
        }

    }


}