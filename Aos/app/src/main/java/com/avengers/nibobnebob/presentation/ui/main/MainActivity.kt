package com.avengers.nibobnebob.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.ActivityMainBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import com.avengers.nibobnebob.presentation.base.NetWorkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()
    override val activityViewModel: BaseActivityViewModel
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNetworkStateObserver()
        initView()
    }

    private fun initView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        with(binding) {
            bnvNavBar.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeFragment || destination.id == R.id.followFragment || destination.id == R.id.myPageFragment) {
                    bnvNavBar.visibility = View.VISIBLE
                } else {
                    bnvNavBar.visibility = View.GONE
                }
            }
        }

    }



}