package com.iti.android.team1.ebuy.ui.login_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.databinding.FragmentLoginScreenBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModel
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModelFactory
import com.iti.android.team1.ebuy.util.AuthRegex

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
           /* if (AuthRegex.isEmailValid(binding.edtEmail.editableText.toString()) &&
                AuthRegex.isPasswordValid(binding.edtPassword.editableText.toString())
            ) {
                ///TODO: make login call

            } else if (!AuthRegex.isEmailValid(binding.edtEmail.editableText.toString())) {
                ///TODO: email is week
                binding.textInputLayout.error = "Email is not valid"
            } else if (!AuthRegex.isPasswordValid(binding.edtPassword.editableText.toString())) {
                ///TODO: password not valid
                binding.textInputLayout2.error = "Password is not valid"

            }*/
        }
    }

}