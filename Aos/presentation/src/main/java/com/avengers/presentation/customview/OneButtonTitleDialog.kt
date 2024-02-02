package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.avengers.presentation.databinding.DialogOneButtonTitleBinding

class OneButtonTitleDialog(
    context: Context,
    private val title: String,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogOneButtonTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogOneButtonTitleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvTitle.text = title
        tvConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
    }
}