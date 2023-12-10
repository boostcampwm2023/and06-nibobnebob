package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.avengers.nibobnebob.databinding.DialogRecommendRestaurantBinding
import com.avengers.nibobnebob.presentation.ui.main.home.adapter.HomeRecommendAdapter
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRecommendRestaurantData

class RecommendRestaurantDialog(
    context: Context,
    private val uiRecommendRestaurantDataList: List<UiRecommendRestaurantData>,
    private val restaurantClickListener: (Int) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogRecommendRestaurantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRecommendRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val adapter = HomeRecommendAdapter { id ->
            restaurantClickListener.invoke(id)
            dismiss()
        }

        rvRecommendRestaurant.adapter = adapter
        adapter.submitList(uiRecommendRestaurantDataList)

        if(uiRecommendRestaurantDataList.isEmpty()){
            binding.tvIsEmpty.visibility = View.VISIBLE
        }


        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

}
