package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentMyPageBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view : View){
        navController = Navigation.findNavController(view)
        with(binding) {
            vEditProfile.setOnClickListener {
                navController.navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToEditProfileFragment()
                )
            }
            vMyList.setOnClickListener {
                navController.navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToMyRestaurantListFragment()
                )
            }
            vWishList.setOnClickListener {
                navController.navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToWishRestaurantListFragment()
                )
            }
        }

    }

}