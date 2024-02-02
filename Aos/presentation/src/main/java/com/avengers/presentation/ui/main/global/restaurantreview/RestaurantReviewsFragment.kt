package com.avengers.nibobnebob.presentation.ui.main.global.restaurantreview

import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.customBack
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail.adapter.RestaurantReviewAdapter
import com.avengers.nibobnebob.presentation.util.Constants
import com.avengers.presentation.R
import com.avengers.presentation.databinding.FragmentRestaurantReviewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewsFragment :
    BaseFragment<FragmentRestaurantReviewsBinding>(R.layout.fragment_restaurant_reviews) {
    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RestaurantReviewsViewModel by viewModels()

    private val args: RestaurantReviewsFragmentArgs by navArgs()
    private val restaurantId by lazy { args.restaurantId }
    private val restaurantName by lazy { args.restaurantName }
    private val adapter = RestaurantReviewAdapter()

    override fun initView() = with(binding) {
        vm = viewModel
        setFilterMenu()
        viewModel.getAllReviews(restaurantId, restaurantName)

        rvReview.adapter = adapter
        rvReview.animation = null
        rvReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val scrollBottom = !rvReview.canScrollVertically(1)
                val hasNextPage = viewModel.uiState.value.lastPage
                val isNotLoading = !viewModel.uiState.value.isLoading

                if (scrollBottom && hasNextPage && isNotLoading) {
                    viewModel.loadNextPage()
                }
            }
        })
        customBack(requireActivity(), findNavController())

    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is RestaurantReviewsEvents.NavigateToBack -> findNavController().navigateUp()
                    is RestaurantReviewsEvents.ShowSnackMessage -> showSnackBar(event.msg)
                    else -> Unit
                }
            }
        }
    }

    override fun initNetworkView() {
    }

    private fun setFilterMenu() {

        binding.tvFilter.setOnClickListener {
            PopupMenu(requireContext(), binding.tvFilter).apply {
                menuInflater.inflate(R.menu.review_filter_menu, menu)
                setOnMenuItemClickListener {
                    adapter.submitList(emptyList())
                    viewModel.getAllReviews(
                        id = restaurantId,
                        name = restaurantName,
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