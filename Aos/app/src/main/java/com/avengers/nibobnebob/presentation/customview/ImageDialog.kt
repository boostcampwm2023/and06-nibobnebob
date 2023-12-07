package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.avengers.nibobnebob.databinding.DialogImageViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageDialog(context: Context, private val imageUrl: String) : Dialog(context) {
    private lateinit var binding: DialogImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogImageViewBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        with(binding) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Glide
                .with(context)
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivImage)
            ivImage.setOnClickListener {
                dismiss()
            }
        }

    }


}