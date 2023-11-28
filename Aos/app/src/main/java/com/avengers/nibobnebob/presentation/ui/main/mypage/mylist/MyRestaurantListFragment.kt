package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyRestaurantListBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRestaurantListFragment :
    BaseFragment<FragmentMyRestaurantListBinding>(R.layout.fragment_my_restaurant_list) {

    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val viewModel: MyRestaurantListViewModel by viewModels()
    private lateinit var navController: NavController
    override val parentViewModel: MainViewModel by activityViewModels()
    private val adapter = MyRestaurantAdapter { id ->
        viewModel.clickItem(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        viewModel.myRestaurantList()
    }

    private fun initView(view: View) {
        binding.svm = sharedViewModel
        binding.vm = viewModel
        binding.rvMyRestaurant.adapter = adapter

        navController = Navigation.findNavController(view)


        viewLifecycleOwner.repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToBack ->
                        navController.navigate(MyRestaurantListFragmentDirections.globalToMyPageFragment())

                    else -> Unit
                }

            }
        }

    }

}