package com.iti.android.team1.ebuy.ui.add_address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.android.team1.ebuy.databinding.FragmentAddAddressBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddAddressViewModel
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddViewModelFactory

class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding

    private val viewModel: AddAddressViewModel by viewModels {
        AddViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }



}