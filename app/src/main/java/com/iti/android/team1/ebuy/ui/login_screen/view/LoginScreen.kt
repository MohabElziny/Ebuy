package com.iti.android.team1.ebuy.ui.login_screen.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentLoginScreenBinding
import com.iti.android.team1.ebuy.main.view.MainActivity
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.pojo.CustomerLogin
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModel
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModelFactory
import com.iti.android.team1.ebuy.ui.register_screen.AuthResult
import com.iti.android.team1.ebuy.ui.register_screen.ErrorType


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

        fetchUserData()
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
            viewModel.makeLoginRequest(
                CustomerLogin(
                    email = binding.edtEmail.text.toString().trim(),
                    password = binding.edtPassword.text.toString().trim()
                )
            )

        }
    }

    private fun fetchUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.observe(viewLifecycleOwner) { result ->
                when (result) {

                    is AuthResult.InvalidData -> {
                        when (result.error) {
                            ErrorType.EmailError -> binding.edtEmail.error =
                                getString(R.string.invalid_email)
                            ErrorType.PasswordError -> binding.edtPassword.error =
                                getString(R.string.invalid_password)
                        }
                    }

                    AuthResult.Loading -> {
                        binding.btnLogin.isClickable = false
                        binding.btnLogin.isFocusable = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }

                    is AuthResult.RegisterFail -> {

                        val builder = AlertDialog.Builder(requireContext())

                        builder.setTitle(getString(R.string.auth_error))
                        builder.setMessage(getString(R.string.invailed_auth_data))

                        builder.setPositiveButton(android.R.string.ok) { _, _ ->
                            binding.btnLogin.isClickable = true
                            binding.btnLogin.isFocusable = true
                        }.show()

                        binding.ProgressBar.visibility = View.GONE
                    }

                    is AuthResult.RegisterSuccess -> {
                        viewModel.setUserIdToPrefs(result.customer.id ?: 1L)
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