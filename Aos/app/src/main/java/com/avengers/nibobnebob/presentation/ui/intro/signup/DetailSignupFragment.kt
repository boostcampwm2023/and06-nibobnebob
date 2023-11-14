package com.avengers.nibobnebob.presentation.ui.intro.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentDetailSignupBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSignupFragment :
    BaseFragment<FragmentDetailSignupBinding>(R.layout.fragment_detail_signup) {

    private val viewModel: DetailSignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setGenderRadioListener()
    }

    private fun setGenderRadioListener(){
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_gender_female -> viewModel.setGender(Gender.FEMALE)
                R.id.rb_gender_male -> viewModel.setGender(Gender.MALE)
            }
        }
    }
}

@BindingAdapter("helperMessage")
fun bindHelpMessage(view: TextView, inputState: InputState) {
    when (inputState) {
        is InputState.Success -> {
            view.text = inputState.msg
            view.setTextColor(ContextCompat.getColor(view.context, R.color.nn_primary6))
        }

        is InputState.Error -> {
            view.text = inputState.msg
            view.setTextColor(ContextCompat.getColor(view.context, R.color.nn_rainbow_red))
        }

        else -> {
            view.text = ""
        }
    }
}

@BindingAdapter("textLayoutColor")
fun bindTextLayoutColor(view: TextInputLayout, inputState: InputState) {
    when (inputState) {
        is InputState.Success -> view.boxStrokeColor = ContextCompat.getColor(view.context, R.color.nn_primary6)
        is InputState.Error -> view.boxStrokeColor = ContextCompat.getColor(view.context, R.color.nn_rainbow_red)
        else -> view.boxStrokeColor = ContextCompat.getColor(view.context, R.color.nn_primary6)
    }
}