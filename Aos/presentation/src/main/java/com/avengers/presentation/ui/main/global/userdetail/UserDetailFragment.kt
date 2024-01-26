package com.avengers.nibobnebob.presentation.ui.main.global.userdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.ImageDialog
import com.avengers.nibobnebob.presentation.ui.customBack
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.global.userdetail.adapter.UserDetailRestaurantAdapter
import com.avengers.presentation.R
import com.avengers.presentation.databinding.FragmentUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : BaseFragment<FragmentUserDetailBinding>(R.layout.fragment_user_detail) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: UserDetailViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()
    private val nickName by lazy { args.nickName }
    private val adapter = UserDetailRestaurantAdapter { id -> viewModel.restaurantDetail(id) }

    override fun initView() = with(binding) {
        vm = viewModel
        viewModel.setNick(nickName)
        rvRestaurant.adapter = adapter
        customBack(requireActivity(), findNavController())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initNetworkView() {
        viewModel.getUserDetail()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is UserDetailEvents.NavigateToRestaurantDetail ->
                        findNavController().toRestaurantDetail(
                            it.id
                        )

                    is UserDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is UserDetailEvents.ShowSnackMessage -> showSnackBar(it.msg)
                    is UserDetailEvents.ShowToastMessage -> showToastMessage(it.msg)
                    is UserDetailEvents.ShowBiggerImageDialog -> ImageDialog(
                        requireContext(),
                        it.img
                    ).show()
                }
            }
        }
    }


    private fun NavController.toRestaurantDetail(id: Int) {
        val action =
            UserDetailFragmentDirections.actionUserDetailFragmentToRestaurantDetailFragment(id)
        navigate(action)
    }
}