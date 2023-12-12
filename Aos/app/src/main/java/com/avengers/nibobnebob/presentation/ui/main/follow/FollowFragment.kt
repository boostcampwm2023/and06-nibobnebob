package com.avengers.nibobnebob.presentation.ui.main.follow

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.follow.adapter.FollowAdapter
import com.avengers.nibobnebob.presentation.ui.toUserDetail
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : BaseFragment<FragmentFollowBinding>(R.layout.fragment_follow) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
        binding.rvFollowList.adapter = FollowAdapter()
        binding.rvRecommendFriend.adapter = FollowAdapter()
        setTabSelectedListener()
        finishApp()
    }

    override fun initNetworkView() {
        viewModel.getMyFollower()
        viewModel.getMyRecommendFollow()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is FollowEvents.NavigateToFollowSearch -> findNavController().toFollowSearch()
                    is FollowEvents.NavigateToUserDetail -> findNavController().toUserDetail(it.nickName)
                    is FollowEvents.ShowToastMessage -> showToastMessage(it.msg)
                    is FollowEvents.ShowSnackMessage -> showSnackBar(it.msg)
                }
            }
        }
    }

    private fun setTabSelectedListener() {
        binding.tbFollowingTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.getMyFollower()
                    1 -> viewModel.getMyFollowing()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun finishApp(){
        var backPressTime = 0L
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(System.currentTimeMillis() - backPressTime <= 2000) {
                    parentViewModel.finishApp()
                } else{
                    backPressTime = System.currentTimeMillis()
                    showToastMessage("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.")
                }
            }
        })
    }

    private fun NavController.toFollowSearch() {
        val action = FollowFragmentDirections.actionFollowFragmentToFollowSearchFragment()
        navigate(action)
    }

}