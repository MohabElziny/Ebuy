package com.iti.android.team1.ebuy.activities.auth.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.activities.main.connection.ConnectionLiveData
import com.iti.android.team1.ebuy.activities.main.connection.DoesNetworkHaveInternet
import com.iti.android.team1.ebuy.databinding.ActivityAuthBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding
    private lateinit var fragmentContainer: FragmentContainerView

    private val viewModel: ConnectionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        fragmentContainer = findViewById(R.id.nav_host_fragment_activity_auth)
        navController = findNavController(R.id.nav_host_fragment_activity_auth)
        setDefault()
        setConnectionState()
        navController.addOnDestinationChangedListener(this::onDestinationChanged)
    }

    private fun setDefault() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2, R.id.onBoardingFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setConnectionState() {
        ConnectionLiveData(this).observe(this) { connection ->
            viewModel.updateConnection(connection)
        }
    }

    override fun onResume() {
        super.onResume()
        checkResumeConnection()
    }

    private fun checkResumeConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateConnection(DoesNetworkHaveInternet.execute())
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.onBoardingFragment -> binding.appBarLayout.visibility = View.GONE
            else -> binding.appBarLayout.visibility = View.VISIBLE
        }
    }
}