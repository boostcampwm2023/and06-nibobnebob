package com.avengers.nibobnebob.presentation.ui.intro.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.BuildConfig
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentLoginBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val TAG = "LoginFragmentDebug"
    private val viewModel : LoginViewModel by viewModels()
    private var email : String = ""
    private var pic : String = ""
    private var token : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverIdLoginSDK.initialize(requireContext(), BuildConfig.NAVER_LOGIN_CLIENT_ID, BuildConfig.NAVER_LOGIN_CLIENT_SECRET, "gyroh")
        NaverIdLoginSDK.showDevelopersLog(true)

        binding.btnNaver.setOnClickListener {
            naverLogin()
        }
        binding.logoutTest.setOnClickListener {
            naverLogout()
        }
    }

    private fun naverLogin(){
        val oAuthLoginCallback = object : OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d(TAG,"errorCode:$errorCode, errorDesc:$errorDescription")
            }

            override fun onSuccess() {
                Log.d(TAG,NaverIdLoginSDK.getAccessToken().toString())
                binding.loginTest.text = NaverIdLoginSDK.getAccessToken().toString()
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oAuthLoginCallback)
    }

    private fun naverLogout(){
        NaverIdLoginSDK.logout()
    }
}