package com.iti.android.team1.ebuy.ui.nointernet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentNoInternetBinding
import kotlinx.coroutines.flow.buffer

class NoInternetFragment : Fragment() {

    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConnectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleAppBar(connect: Boolean) {
        if (connect)
            (requireActivity() as MainActivity).noConnectionNavigation()
        else
            (requireActivity() as MainActivity).setDefault()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAppBar(true)
        handleNetworkBack()
    }

    private fun handleNetworkBack() {
        lifecycleScope.launchWhenStarted {
            viewModel.isConnected.buffer().collect {
                if (it) {
                    findNavController().navigate(R.id.action_noInternetFragment_to_navigation_home)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handleAppBar(false)
        _binding = null
    }

}