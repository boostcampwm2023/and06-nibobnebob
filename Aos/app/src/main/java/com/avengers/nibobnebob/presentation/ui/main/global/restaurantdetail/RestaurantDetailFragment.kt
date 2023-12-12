package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.TwoButtonTitleDialog
import com.avengers.nibobnebob.presentation.ui.customBack
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail.adapter.RestaurantReviewAdapter
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
        customBack(requireActivity(), findNavController())
    }

    override fun initNetworkView() {
        viewModel.restaurantDetail()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is RestaurantDetailEvents.NavigateToBack -> findNavController().navigateUp()

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

                    is RestaurantDetailEvents.NavigateToReviewPage -> {
                        findNavController().toReviewPage(event.restaurantName, event.restaurantId)
                    }

                    is RestaurantDetailEvents.ShowSnackMessage -> showSnackBar(event.msg)
                    is RestaurantDetailEvents.ShowToastMessage -> showToastMessage(event.msg)
                }
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

    private fun NavController.toReviewPage(name: String, id: Int) {
        val action =
            RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToRestaurantReviewsFragment(
                name,
                id
            )
        navigate(action)
    }
}