package com.avengers.nibobnebob.presentation.ui.intro.login

import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroViewModel
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import com.avengers.presentation.R
import com.avengers.presentation.databinding.FragmentLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val TAG = "LoginFragmentDebug"
    private val viewModel: LoginViewModel by viewModels()
    override val parentViewModel: IntroViewModel by activityViewModels()

    override fun initNetworkView() {}

    override fun initView() {
        binding.vm = viewModel
        binding.btnNaver.setOnClickListener {
            naverLogin()
        }
        checkAutoLogin()
    }

    override fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is LoginEvent.NavigateToMain -> findNavController().toMainActivity()
                    is LoginEvent.NavigateToDialog -> {
                        // TODO : FINAL -> 다이얼로그
                    }

                    is LoginEvent.NavigateToBasicSignup -> findNavController().toBasicSignup()
                    is LoginEvent.NavigateToDetailSignup -> findNavController().toDetailSignup()
                    is LoginEvent.ShowSnackMessage -> showSnackBar(it.msg)
                }
            }
        }
    }

    private fun naverLogin() {
        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d(TAG, "errorCode:$errorCode, errorDesc:$errorDescription")
                //TODO : FINAL -> 실패 다이얼로그 띄워주기
            }

            override fun onSuccess() {
                val token = NaverIdLoginSDK.getAccessToken().toString()
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Log.d(TAG, "errorCode:$errorCode, errorDesc:$errorDescription")
                    }

                    override fun onSuccess(result: NidProfileResponse) {
                        viewModel.naverEmail.value = result.profile?.email.toString()
                        viewModel.loginNaver(token)
                    }
                })
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oAuthLoginCallback)
    }

    private fun checkAutoLogin() {
        binding.cbAutoLogin.setOnCheckedChangeListener { buttonView, _ ->
            viewModel.setAutoLogin(buttonView.isChecked)
        }
    }

    private fun NavController.toDetailSignup() {
        val action = LoginFragmentDirections.actionLoginFragmentToDetailSignupFragment(
            provider = "naver",
            email = viewModel.naverEmail.value
        )
        this.navigate(action)
    }

    private fun NavController.toBasicSignup() {
        val action = LoginFragmentDirections.actionLoginFragmentToBasicSignupFragment()
        navigate(action)
    }

    private fun NavController.toMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}