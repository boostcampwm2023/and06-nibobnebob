package com.avengers.nibobnebob.presentation.ui.main.mypage.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentWishRestaurantListBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.mylist.MyRestaurantAdapter
import com.avengers.nibobnebob.presentation.ui.main.mypage.mylist.MyRestaurantEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.ui.toRestaurantDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishRestaurantListFragment :
    BaseFragment<FragmentWishRestaurantListBinding>(R.layout.fragment_wish_restaurant_list) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyWishListViewModel by viewModels()
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val adapter = MyWishAdapter({ id -> viewModel.myWishList() }, { id -> viewModel.showDetail(id) },
        { id -> viewModel.deleteMyList(id) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        viewModel.myWishList()
    }

    private fun initView(view: View) {
        binding.svm = sharedViewModel
        binding.vm = viewModel
        binding.rvWishRestaurant.adapter = adapter
        binding.rvWishRestaurant.animation = null

        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToBack ->
                        findNavController().navigate(WishRestaurantListFragmentDirections.globalToMyPageFragment())

                    else -> Unit
                }

            }
        }

        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MyWishEvent.NavigateToRestaurantDetail -> findNavController().toRestaurantDetail(
                        event.id
                    )

                    is MyWishEvent.ShowToastMessage -> showToastMessage(event.msg)
                    is MyWishEvent.ShowSnackMessage -> showSnackBar(event.msg)
                }
            }
        }

    }

}