package com.iti.android.team1.ebuy.ui.Splah

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels {
        SplashFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            delay(2000)

            findNavController().popBackStack()
            if (viewModel.isRunFirstTime()) {
                findNavController().navigate(R.id.onBoardingFragment)
                viewModel.setRunFirstTime()
            } else {
                if (viewModel.getAuthStateFromPrefs())
                    navigateToMainActivity()
                else findNavController().navigate(R.id.loginScreen2)
            }
        }

        requireActivity().findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
    }

    private fun navigateToMainActivity() {
        requireContext().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

}