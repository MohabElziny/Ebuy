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
    var connect = true

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
        handleConnection()
        navController.addOnDestinationChangedListener(this::onDestinationChanged)
    }

    fun setDefault() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2, R.id.onBoardingFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun noConnectionNavigation() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2, R.id.onBoardingFragment,
                R.id.noInternetFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setConnectionState() {
        ConnectionLiveData(this).observe(this) { connection ->
            viewModel.updateConnection(connection)

        }
    }

    private fun handleConnection() {
        lifecycleScope.launchWhenStarted {
            viewModel.isConnected.buffer().collect { connection ->
                connect = connection
                if (!connection) {
                    handleNotConnected()
                }
            }
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

    private fun handleNotConnected() {
        showSnackBar(getString(R.string.not_connected))
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .show()
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

    override fun onBackPressed() {
        if (!connect)
            finish()
        else {
            super.onBackPressed()
        }
    }
}