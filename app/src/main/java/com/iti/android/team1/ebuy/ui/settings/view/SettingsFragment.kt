package com.iti.android.team1.ebuy.ui.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.android.team1.ebuy.databinding.FragmentSettingsBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.settings.viewmodel.SettingsViewModel
import com.iti.android.team1.ebuy.ui.settings.viewmodel.SettingsViewModelFactory

class SettingsFragment : Fragment() {

    private lateinit var bindding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindding = FragmentSettingsBinding.inflate(inflater, container, false)
        return bindding.root
    }


}