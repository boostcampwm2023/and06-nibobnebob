package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.avengers.nibobnebob.databinding.DialogTwoButtonBinding

class TwoButtonDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogTwoButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTwoButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvTitle.text = title
        tvDescription.text = description
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
    }
}