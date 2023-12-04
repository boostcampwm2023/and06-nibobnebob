package com.avengers.nibobnebob.presentation.ui.main.mypage.edit

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentEditProfileBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.CalendarDatePicker
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val viewModel: EditProfileViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun initView() {
        binding.svm = sharedViewModel
        binding.vm = viewModel
        view?.let { navController = Navigation.findNavController(it) }
        setDateBtnListener()
    }

    override fun initNetworkView() {
        //TODO : 네트워크
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is EditProfileUiEvent.EditProfileDone -> {
                        navController.navigate(EditProfileFragmentDirections.globalToMyPageFragment())
                    }

                    is EditProfileUiEvent.ShowToastMessage -> showToastMessage(event.msg)
                    is EditProfileUiEvent.ShowSnackMessage -> showSnackBar(event.msg)
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
            CalendarDatePicker {
                viewModel.setBirth(it)
            }.show(parentFragmentManager)
        }
    }

}