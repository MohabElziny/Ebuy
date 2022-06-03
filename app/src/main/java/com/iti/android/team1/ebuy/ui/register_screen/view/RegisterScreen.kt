package com.iti.android.team1.ebuy.ui.register_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.databinding.FragmentRegisterScreenBinding
import com.iti.android.team1.ebuy.ui.register_screen.viewmodel.RegisterScreenViewModel

class RegisterScreen : Fragment() {

    companion object {
        fun newInstance() = RegisterScreen()
    }

    private var _binding:FragmentRegisterScreenBinding? = null
    private lateinit var viewModel: RegisterScreenViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterScreenBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.btnSignIn.setOnClickListener {
                findNavController().popBackStack()
            }
    }

}