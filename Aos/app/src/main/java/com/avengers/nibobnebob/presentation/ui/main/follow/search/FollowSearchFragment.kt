package com.avengers.nibobnebob.presentation.ui.main.follow.search

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.SelectRegionDialog
import com.avengers.nibobnebob.presentation.ui.adjustKeyboard
import com.avengers.nibobnebob.presentation.ui.customBack
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.follow.adapter.FollowSearchAdapter
import com.avengers.nibobnebob.presentation.ui.toUserDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowSearchFragment :
    BaseFragment<FragmentFollowSearchBinding>(R.layout.fragment_follow_search) {
    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowSearchViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
        binding.rvFollowSearch.adapter = FollowSearchAdapter()
        binding.rvFollowSearch.itemAnimator = null
        setEditText()
        viewModel.observeKeyword()
        customBack(requireActivity(), findNavController())
    }

    override fun initNetworkView() {
        //TODO : 네트워크
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is FollowSearchEvents.NavigateToUserDetail -> findNavController().toUserDetail(
                        it.nickName
                    )

                    is FollowSearchEvents.ShowSnackMessage -> showSnackBar(it.msg)
                    is FollowSearchEvents.ShowFilterDialog -> {
                        SelectRegionDialog(
                            requireContext(),
                            it.curRegion,
                            it.changeFilterListener
                        ).show()
                    }

                    is FollowSearchEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setEditText() {
        binding.etSearch.requestFocus()
        requireContext().adjustKeyboard(binding.etSearch, true)
    }
}