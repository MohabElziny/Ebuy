package com.iti.android.team1.ebuy.ui.login_screen.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.ui.activities.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.ui.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentLoginScreenBinding
import com.iti.android.team1.ebuy.data.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.data.repository.Repository
import com.iti.android.team1.ebuy.data.pojo.CustomerLogin
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModel
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModelFactory
import com.iti.android.team1.ebuy.ui.register_screen.AuthResult
import com.iti.android.team1.ebuy.ui.register_screen.ErrorType
import com.iti.android.team1.ebuy.util.showSnackBar
import com.iti.android.team1.ebuy.util.trimText
import kotlinx.coroutines.flow.buffer

class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<ConnectionViewModel>()
    private val viewModel: LoginScreenViewModel by viewModels {
        LoginScreenViewModelFactory(Repository(LocalSource(requireContext())))
    }
    var isInternetConnected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

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
        handleNoConnection()
        binding.btnLogin.setOnClickListener {
            if (isInternetConnected)
                viewModel.makeLoginRequest(
                    CustomerLogin(
                        email = binding.edtEmail.trimText(),
                        password = binding.edtPassword.trimText()
                    )
                )
            else
                showSnackBar(getString(R.string.not_connected))
        }
    }

    private fun handleNoConnection() {
        lifecycleScope.launchWhenCreated {
            sharedViewModel.isConnected.buffer().collect { connect ->
                isInternetConnected = connect
            }
        }
    }

    private fun fetchUserData() {
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


    private fun navigateToMainActivity() {
        requireContext().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}