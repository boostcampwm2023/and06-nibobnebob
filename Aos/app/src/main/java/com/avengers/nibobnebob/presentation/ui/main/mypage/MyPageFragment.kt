package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.data.remote.IntroApi
import com.avengers.nibobnebob.databinding.FragmentMyPageBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view: View) {
        binding.svm = sharedViewModel
        binding.vm = viewModel

        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToEditProfile -> findNavController().toEditProfile()
                    is MyPageSharedUiEvent.NavigateToMyList -> findNavController().toMyList()
                    is MyPageSharedUiEvent.NavigateToWishList -> findNavController().toWishList()
                    else -> Unit
                }

            }
        }

        repeatOnStarted {
            viewModel.events.collect{ event ->
                when(event){
                    is MyEditPageEvent.NavigateToIntro -> {
                        val intent = Intent(context, IntroActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun initNetworkView() {
        // todo 데이터 통신으로 그려지는 View 생성 로직
    }

    private fun NavController.toEditProfile() {
        val action = MyPageFragmentDirections.globalToEditProfileFragment()
        this.navigate(action)
    }

    private fun NavController.toMyList() {
        val action = MyPageFragmentDirections.globalToMyRestaurantListFragment()
        this.navigate(action)
    }

    private fun NavController.toWishList() {
        val action = MyPageFragmentDirections.globalToWishRestaurantListFragment()
        this.navigate(action)
    }
}