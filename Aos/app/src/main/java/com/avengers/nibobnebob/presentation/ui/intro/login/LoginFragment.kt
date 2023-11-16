package com.avengers.nibobnebob.presentation.ui.intro.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.BuildConfig
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentLoginBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroViewModel
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

        NaverIdLoginSDK.initialize(
            requireContext(),
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET, TAG)
        NaverIdLoginSDK.showDevelopersLog(true)


        initEventObserver()
        binding.btnNaver.setOnClickListener {
            naverLogin()
        }

    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.eventFlow.collect{
                when(it){
                    LoginViewModel.LoginEvent.LoginSuccess -> {
                        //회원가입으로 이동
                    }
                    LoginViewModel.LoginEvent.LoginFailure -> {
                        //다이얼로그 띄우기
                    }
                }
            }
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
                //TODO : FINAL -> 실패 다이얼로그 띄워주기
            }

            override fun onSuccess() {
                viewModel.token.value = NaverIdLoginSDK.getAccessToken().toString()
                viewModel.postNaverLogin()
                // TODO : datastore이든 sharedpref이든 서버와의 통신 진행 후 토큰을 저장 해야함 (repository에서, 추가한 preferencesdatastore에 진행)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oAuthLoginCallback)
    }

    private fun naverLogout(){
        NaverIdLoginSDK.logout()
    }
}