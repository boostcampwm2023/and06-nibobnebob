package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyPageBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var navController: NavController
    private val viewModel : MyPageViewModel by viewModels()
    private val sharedViewModel : MyPageSharedViewModel by viewModels ()
    override val parentViewModel: MainViewModel by activityViewModels()

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
                    is MyPageSharedUiEvent.NavigateToEditProfile ->
                        navController.navigate(MyPageFragmentDirections.globalToEditProfileFragment())
                    is MyPageSharedUiEvent.NavigateToMyList ->
                        navController.navigate(MyPageFragmentDirections.globalToMyRestaurantListFragment())
                    is MyPageSharedUiEvent.NavigateToWishList ->
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

    private fun initNetworkView(){
        // todo 데이터 통신으로 그려지는 View 생성 로직
    }
}