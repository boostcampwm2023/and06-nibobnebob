package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail

import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.TwoButtonTitleDialog
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail.adapter.RestaurantReviewAdapter
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_BEST
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_NEW
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_OLD
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_WORST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment :
    BaseFragment<FragmentRestaurantDetailBinding>(R.layout.fragment_restaurant_detail) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RestaurantDetailViewModel by viewModels()

    val args: RestaurantDetailFragmentArgs by navArgs()
    private val restaurantId by lazy { args.restaurantId }

    override fun initView() {
        binding.vm = viewModel
        viewModel.setRestaurantId(restaurantId)
        binding.rvReview.adapter = RestaurantReviewAdapter()
        setFilterMenu()
    }

    override fun initNetworkView() {
        viewModel.restaurantDetail()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is RestaurantDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is RestaurantDetailEvents.NavigateToDetailReview -> {
                        // 상세리뷰로 이동
                    }

                    is RestaurantDetailEvents.NavigateToDeleteMyList -> {
                        TwoButtonTitleDialog(
                            requireContext(),
                            getString(R.string.delete_my_list_label),
                            getString(R.string.delete_my_list),
                            ::deleteClicked
                        ).show()
                    }

                    is RestaurantDetailEvents.NavigateToAddMyList -> {
                        findNavController().toAddRestaurantLocal(
                            viewModel.uiState.value.name,
                            viewModel.restaurantId.value
                        )
                    }

                    is RestaurantDetailEvents.ShowSnackMessage -> showSnackBar(event.msg)
                    is RestaurantDetailEvents.ShowToastMessage -> showToastMessage(event.msg)
                }
            }
        }
    }

    private fun setFilterMenu() {

        binding.tvFilter.setOnClickListener {
            PopupMenu(requireContext(), binding.ivFilter).apply {
                menuInflater.inflate(R.menu.review_filter_menu, menu)
                setOnMenuItemClickListener {
                    viewModel.sortReview(
                        sort =
                        when (it.itemId) {
                            R.id.menu_new -> FILTER_NEW
                            R.id.menu_old -> FILTER_OLD
                            R.id.menu_best -> FILTER_BEST
                            R.id.menu_worst -> FILTER_WORST
                            else -> null
                        }
                    )

                    true
                }
                show()
            }
        }

    }


    private fun deleteClicked() {
        viewModel.deleteMyList()
    }

    private fun NavController.toAddRestaurantLocal(name: String, id: Int) {
        val action =
            RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToAddMyRestaurantFragment(
                name,
                id
            )
        navigate(action)
    }
}