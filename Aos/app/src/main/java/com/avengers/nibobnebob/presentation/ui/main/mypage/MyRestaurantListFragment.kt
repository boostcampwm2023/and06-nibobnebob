package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyRestaurantListBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment

class MyRestaurantListFragment :
    BaseFragment<FragmentMyRestaurantListBinding>(R.layout.fragment_my_restaurant_list) {

    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view: View) {
        binding.vm = sharedViewModel

        navController = Navigation.findNavController(view)


        viewLifecycleOwner.repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageUiEvent.NavigateToBack ->
                        navController.navigateUp()

                    else -> Unit
                }

            }
        }

    }

}