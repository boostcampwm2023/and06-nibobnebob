package com.avengers.nibobnebob.presentation.ui.main.follow

import android.os.Bundle
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

class FollowFragment : BaseFragment<FragmentFollowBinding>(R.layout.fragment_follow) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvFollowList.adapter = FollowAdapter()
        initEventObserver()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is FollowEvents.NavigateToFollowSearch -> findNavController().toFollowSearch()
                    is FollowEvents.NavigateToFollowDetail -> findNavController().toFollowDetail(it.nickName)
                    is FollowEvents.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
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