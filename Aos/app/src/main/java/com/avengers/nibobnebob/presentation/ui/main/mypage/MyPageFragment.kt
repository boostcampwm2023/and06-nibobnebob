package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyPageBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var navController: NavController
    private val viewModel : MyPageViewModel by viewModels()
    private val sharedViewModel : MyPageSharedViewModel by viewModels ()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view : View){
        binding.vm = sharedViewModel
        navController = Navigation.findNavController(view)


        viewLifecycleOwner.repeatOnStarted {
            sharedViewModel.uiEvent.collect{ event ->
                when(event){
                    is MyPageUiEvent.NavigateToEditProfile ->
                        navController.navigate(MyPageFragmentDirections.globalToEditProfileFragment())
                    is MyPageUiEvent.NavigateToMyList ->
                        navController.navigate(MyPageFragmentDirections.globalToMyRestaurantListFragment())
                    is MyPageUiEvent.NavigateToWishList ->
                        navController.navigate(MyPageFragmentDirections.globalToWishRestaurantListFragment())
                    else -> Unit
                }

            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                binding.uiState = state
            }
        }



    }

}