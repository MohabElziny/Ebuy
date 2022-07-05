package com.iti.android.team1.ebuy.ui.settings.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.ui.activities.auth.view.AuthActivity
import com.iti.android.team1.ebuy.databinding.FragmentSettingsBinding
import com.iti.android.team1.ebuy.data.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.data.repository.Repository
import com.iti.android.team1.ebuy.ui.settings.viewmodel.SettingsViewModel
import com.iti.android.team1.ebuy.ui.settings.viewmodel.SettingsViewModelFactory

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addressRelative.setOnClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToAddressesFragment()
            )
        }

        binding.logoutRelative.setOnClickListener {
            showLogoutAlertDialog()
        }
    }

    private fun showLogoutAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.settings_logout))
            .setMessage(getString(R.string.log_out_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.settings_logout)) { dialog, _ ->
                viewModel.logOut()
                dialog.dismiss()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                activity?.finish()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}