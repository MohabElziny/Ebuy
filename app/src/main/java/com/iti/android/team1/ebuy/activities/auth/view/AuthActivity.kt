package com.iti.android.team1.ebuy.activities.auth.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.connection.ConnectionLiveData
import com.iti.android.team1.ebuy.databinding.ActivityAuthBinding
import kotlinx.coroutines.flow.buffer

class AuthActivity : AppCompatActivity() {

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
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setConnectionState()
        handleConnection()
    }

    private fun setConnectionState() {
        ConnectionLiveData(this).observe(this) { connection ->
            viewModel.updateConnection(connection)

        }
    }

    private fun handleConnection() {
        lifecycleScope.launchWhenStarted {
            viewModel.isConnected.buffer().collect { connection ->
                if (connection) {
                    handleIsConnected()
                } else {
                    handleNotConnected()
                }
            }
        }
    }

    private fun handleIsConnected() {
        binding.noConnection.root.visibility = View.INVISIBLE
        fragmentContainer.visibility = View.VISIBLE
        binding.appBarLayout.visibility = View.VISIBLE
        showSnackBar(getString(R.string.connected))

    }

    private fun handleNotConnected() {
        fragmentContainer.visibility = View.INVISIBLE
        binding.appBarLayout.visibility = View.INVISIBLE
        binding.noConnection.root.visibility = View.VISIBLE
        showSnackBar(getString(R.string.not_connected))
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .show()
    }
}