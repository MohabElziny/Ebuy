package com.iti.android.team1.ebuy.ui.login_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.android.team1.ebuy.databinding.FragmentLoginScreenBinding
import com.iti.android.team1.ebuy.ui.login_screen.viewmodel.LoginScreenViewModel

class LoginScreen : Fragment() {

    companion object {
        fun newInstance() = LoginScreen()
    }

    private var _binding:FragmentLoginScreenBinding? = null
    private lateinit var viewModel: LoginScreenViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginScreenViewModel::class.java)
        // TODO: Use the ViewModel
    }

}