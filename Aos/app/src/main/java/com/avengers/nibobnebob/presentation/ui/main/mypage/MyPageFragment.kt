package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyPageBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.customview.ImageDialog
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedUiEvent
import com.avengers.nibobnebob.presentation.ui.main.mypage.share.MyPageSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel: MyPageSharedViewModel by viewModels()
    override val parentViewModel: MainViewModel by activityViewModels()

    override fun initView() {
        binding.svm = sharedViewModel
        binding.vm = viewModel
        finishApp()


        binding.tvWithdraw.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("정말 탈퇴하시겠습니까?")
                .setMessage("모든 정보가 삭제됩니다.")
                .setPositiveButton(
                    "확인"
                ) { _, _ -> withDrawConfirm() }
                .setNegativeButton(
                    "취소"
                ) { _, _ -> withDrawDismiss() }
                .create()
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initNetworkView() {
        viewModel.getUserInfo()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            sharedViewModel.uiEvent.collect { event ->
                when (event) {
                    is MyPageSharedUiEvent.NavigateToEditProfile -> findNavController().toEditProfile()
                    is MyPageSharedUiEvent.NavigateToMyList -> findNavController().toMyList()
                    is MyPageSharedUiEvent.NavigateToWishList -> findNavController().toWishList()
                    else -> Unit
                }

            }
        }

        repeatOnStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MyEditPageEvent.NavigateToIntro -> {
                        val intent = Intent(context, IntroActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is MyEditPageEvent.ProfileClicked -> {
                        ImageDialog(requireContext(),event.profileImage).show()
                    }
                    is MyEditPageEvent.ShowSnackMessage -> showSnackBar(event.msg)
                    is MyEditPageEvent.ShowToastMessage -> showToastMessage(event.msg)
                }
            }
        }
    }


    private fun withDrawConfirm() {
        viewModel.withdraw()
    }

    private fun withDrawDismiss() {

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


    private fun NavController.toEditProfile() {
        val action = MyPageFragmentDirections.globalToEditProfileFragment()
        this.navigate(action)
    }

    private fun NavController.toMyList() {
        val action = MyPageFragmentDirections.globalToMyRestaurantListFragment()
        this.navigate(action)
    }

    private fun NavController.toWishList() {
        val action = MyPageFragmentDirections.globalToWishRestaurantListFragment()
        this.navigate(action)
    }
}