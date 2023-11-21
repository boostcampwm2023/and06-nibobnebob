package com.avengers.nibobnebob.presentation.ui.intro.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.BuildConfig
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentLoginBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.intro.IntroViewModel
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {


    private val TAG = "LoginFragmentDebug"
    private val viewModel : LoginViewModel by viewModels()
    override val parentViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        naverInitialize()
        initEventObserver()

        binding.btnNaver.setOnClickListener {
            naverLogin()
        }
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is LoginEvent.NavigateToMain -> findNavController().toMainActivity()
                    is LoginEvent.NavigateToDialog -> {
                        // TODO : FINAL -> 다이얼로그
                    }
                    is LoginEvent.NavigateToDetailSignup -> findNavController().toDetailSignup()
                }
            }
        }
    }

    private fun naverInitialize(){
        NaverIdLoginSDK.initialize(
            requireContext(),
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET, TAG)
        NaverIdLoginSDK.showDevelopersLog(true)
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
                //TODO : FINAL -> 실패 다이얼로그 띄워주기
            }

            override fun onSuccess() {
                val token = NaverIdLoginSDK.getAccessToken().toString()
                viewModel.loginNaver(token)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oAuthLoginCallback)
    }

    private fun NavController.toDetailSignup(){
        val action = LoginFragmentDirections.actionLoginFragmentToDetailSignupFragment()
        this.navigate(action)
    }

    private fun NavController.toMainActivity(){
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        (activity as IntroActivity).finish()
    }
}