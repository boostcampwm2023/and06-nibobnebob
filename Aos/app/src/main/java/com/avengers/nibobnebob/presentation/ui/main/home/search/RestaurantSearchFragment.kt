package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentRestaurantSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.adapter.RestaurantSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantSearchFragment :
    BaseFragment<FragmentRestaurantSearchBinding>(R.layout.fragment_restaurant_search) {
    private val viewModel: RestaurantSearchViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectEvent()

    }

    private fun initView() {
        binding.vm = viewModel
        binding.rvSearch.adapter = RestaurantSearchAdapter{
            viewModel.onClickSearchItem(it.id)
        }
    }

    private fun collectEvent(){
        repeatOnStarted {
            viewModel.events.collect{
                showToastMessage("${(it as RestaurantSearchEvent.OnClickResultItem).id} clicked")
            }
        }
    }
}