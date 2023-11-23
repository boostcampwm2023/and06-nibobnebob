package com.avengers.nibobnebob.presentation.ui.intro.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentDetailSignupBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroViewModel
import com.avengers.nibobnebob.presentation.util.showCalendarDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSignupFragment :
    BaseFragment<FragmentDetailSignupBinding>(R.layout.fragment_detail_signup) {

    private val viewModel: DetailSignupViewModel by viewModels()
    override val parentViewModel: IntroViewModel by activityViewModels()

    private val args: DetailSignupFragmentArgs by navArgs()
    private val email by lazy { args.email }
    private val password by lazy { args.password }
    private val provider by lazy { args.provider }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setDefaultData(email, password, provider)
        initEventsObserver()
        setGenderRadioListener()
        setDateBtnListener()
        setLocationInputListener()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is DetailSignupEvents.NavigateToBack -> findNavController().navigateUp()
                    is DetailSignupEvents.NavigateToLoginFragment -> findNavController().toLoginFragment()
                    is DetailSignupEvents.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun setGenderRadioListener() {
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_gender_female -> viewModel.setIsMale(false)
                R.id.rb_gender_male -> viewModel.setIsMale(true)
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
            simpleItemSelectedColor = ContextCompat.getColor(this.context, R.color.nn_primary1)
            setDropDownBackgroundTint(ContextCompat.getColor(this.context, R.color.nn_primary0))
            setSimpleItems(resources.getStringArray(R.array.location_list))
        }
    }

    private fun NavController.toLoginFragment(){
        val action = DetailSignupFragmentDirections.actionDetailSignupFragmentToLoginFragment()
        this.navigate(action)
    }
}

