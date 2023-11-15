package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentEditProfileBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.util.showCalendarDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.flow.collectLatest

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    private val viewModel :EditProfileViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setDateBtnListener()
        setLocationInputListener()
    }

    private fun initView(view: View) {
        binding.svm = sharedViewModel
        binding.vm = viewModel

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

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                binding.uiState = state
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

    private fun setLocationInputListener() {
        (binding.etLocation as MaterialAutoCompleteTextView).apply {
            simpleItemSelectedColor = resources.getColor(R.color.nn_primary1, null)
            setDropDownBackgroundTint(resources.getColor(R.color.nn_primary0, null))
            setSimpleItems(resources.getStringArray(R.array.location_list))
        }
    }

}