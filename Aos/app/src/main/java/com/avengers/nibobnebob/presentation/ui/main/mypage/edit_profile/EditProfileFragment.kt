package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentEditProfileBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
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
                    is MyPageSharedUiEvent.NavigateToBack ->
                        navController.navigateUp()

                    else -> {
                        Unit
                    }
                }

            }
        }

    }

}