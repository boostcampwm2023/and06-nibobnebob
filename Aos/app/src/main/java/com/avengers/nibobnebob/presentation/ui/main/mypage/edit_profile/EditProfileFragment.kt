package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentEditProfileBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.util.showCalendarDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val viewModel: EditProfileViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        initEventObserver()
        setDateBtnListener()
    }

    private fun initView(view: View) {
        binding.svm = sharedViewModel
        binding.vm = viewModel

        navController = Navigation.findNavController(view)


        repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                binding.uiState = state
            }
        }


    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is EditProfileUiEvent.EditProfileDone -> {
                        navController.navigate(EditProfileFragmentDirections.globalToMyPageFragment())
                        showToastMessage("수정 완료")
                    }
                }
            }
        }

        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToBack ->
                        navController.navigate(EditProfileFragmentDirections.globalToMyPageFragment())

                    else -> Unit

                }

            }
        }
    }

    private fun setDateBtnListener() {
        binding.tilBirth.setEndIconOnClickListener {
            showCalendarDatePicker(parentFragmentManager) {
                viewModel.setBirth(it)
            }
        }
    }

}