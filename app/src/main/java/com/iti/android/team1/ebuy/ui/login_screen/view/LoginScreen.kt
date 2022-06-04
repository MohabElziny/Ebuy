package com.iti.android.team1.ebuy.ui.login_screen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentLoginScreenBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModel
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModelFactory
import com.iti.android.team1.ebuy.util.AuthRegex
import kotlinx.coroutines.flow.buffer


class LoginScreen : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    private val viewModel: LoginScreenViewModel by viewModels {
        LoginScreenViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        if (viewModel.getAuthStateFromPrefs())
            navigateToMainActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(
                LoginScreenDirections.actionLoginScreen2ToRegisterScreen2()
            )
        }

        binding.btnLogin.setOnClickListener {
            if (AuthRegex.isEmailValid(binding.edtEmail.editableText.toString()) &&
                AuthRegex.isPasswordValid(binding.edtPassword.editableText.toString())
            ) {
                viewModel.makeLoginRequest(
                    email = binding.edtEmail.text.toString().trim(),
                    password = binding.edtPassword.text.toString().trim()
                )
                fetchUserData()
            } else if (!AuthRegex.isEmailValid(binding.edtEmail.editableText.toString())) {
                binding.textInputLayout.error = "Email is not valid"
            } else if (!AuthRegex.isPasswordValid(binding.edtPassword.editableText.toString())) {
                binding.textInputLayout2.error = "Wrong password"
            }
        }
    }

    private fun fetchUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.buffer().collect { result ->
                when (result) {
                    is ResultState.Error -> {
                        binding.textInputLayout2.error = result.errorString
                        binding.ProgressBar.visibility = View.GONE
                    }
                    ResultState.Loading -> {
                        binding.btnLogin.isClickable = false
                        binding.btnLogin.isFocusable = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        viewModel.setUserIdToPrefs(result.data.id ?: 1L)
                        viewModel.setAuthStateToPrefs(true)
                        navigateToMainActivity()
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        requireContext().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

}