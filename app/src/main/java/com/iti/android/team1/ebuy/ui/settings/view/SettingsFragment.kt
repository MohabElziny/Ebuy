package com.iti.android.team1.ebuy.ui.settings.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.auth.view.AuthActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindding.addressRelative.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToAddressesFragment()
            )
        }

        bindding.logoutRelative.setOnClickListener {
            viewModel.logOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            activity?.finish()
        }
    }


}