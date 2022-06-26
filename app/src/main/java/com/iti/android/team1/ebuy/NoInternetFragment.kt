package com.iti.android.team1.ebuy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.activities.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentNoInternetBinding
import kotlinx.coroutines.flow.buffer


class NoInternetFragment : Fragment() {

    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConnectionViewModel by activityViewModels()
    private var isHome = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleAppBar(connect: Boolean) {
        when (requireActivity()) {
            is MainActivity -> {
                isHome = true
                if (connect)
                    (requireActivity() as MainActivity).noConnectionNavigation()
                else
                    (requireActivity() as MainActivity).setDefault()
            }
            else ->
                isHome = false
        }
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
//                    if (isHome) {
//                        findNavController().navigate(R.id.action_noInternetFragment_to_navigation_home)
//                    } else {
//                        findNavController().popBackStack()
//                    }
                    requireActivity().finish()
                    requireActivity().startActivity(requireActivity().intent)
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