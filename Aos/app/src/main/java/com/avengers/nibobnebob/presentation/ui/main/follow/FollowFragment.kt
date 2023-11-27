package com.avengers.nibobnebob.presentation.ui.main.follow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.follow.adapter.FollowAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : BaseFragment<FragmentFollowBinding>(R.layout.fragment_follow) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvFollowList.adapter = FollowAdapter()
        initEventObserver()
        setTabSelectedListener()
        viewModel.getMyRecommendFollow()
        viewModel.getMyFollower()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is FollowEvents.NavigateToFollowSearch -> findNavController().toFollowSearch()
                    is FollowEvents.NavigateToFollowDetail -> findNavController().toFollowDetail(it.nickName)
                    is FollowEvents.ShowToastMessage -> showToastMessage(it.msg)
                    else -> {}
                }
            }
        }
    }

    private fun setTabSelectedListener(){
        binding.tbFollowingTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> viewModel.getMyFollower()
                    1 -> viewModel.getMyFollowing()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun NavController.toFollowSearch(){
        val action = FollowFragmentDirections.actionFollowFragmentToFollowSearchFragment()
        navigate(action)
    }

    private fun NavController.toFollowDetail(nickName: String){
        val action = FollowFragmentDirections.actionFollowFragmentToFollowDetailFragment(nickName)
        navigate(action)
    }
}