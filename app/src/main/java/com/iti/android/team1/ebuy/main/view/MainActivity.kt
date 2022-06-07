package com.iti.android.team1.ebuy.main.view

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
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.auth.viewmodel.ConnectionViewModel
import com.iti.android.team1.ebuy.connection.ConnectionLiveData
import com.iti.android.team1.ebuy.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.buffer


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentContainer: FragmentContainerView
    private val viewModel: ConnectionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navView = binding.navView
        fragmentContainer = findViewById(R.id.nav_host_fragment_activity_main)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        setDefault()
        navController.addOnDestinationChangedListener(this::onDestinationChanged)
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
                    navView.setupWithNavController(navController)
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
        binding.navView.visibility = View.VISIBLE
        showSnackBar(getString(R.string.connected))


    }

    private fun handleNotConnected() {
        fragmentContainer.visibility = View.INVISIBLE
        binding.appBarLayout.visibility = View.INVISIBLE
        binding.navView.visibility = View.INVISIBLE
        binding.noConnection.root.visibility = View.VISIBLE
        showSnackBar(getString(R.string.not_connected))
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .show()
    }

    fun setDefault() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_Category, R.id.navigation_profile,
                R.id.navigation_favorites
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun profileNavigation() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_Category, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        val isContained = appBarConfiguration.topLevelDestinations.any {
            it == destination.id
        }
        if (isContained) {
            navView.visibility = View.VISIBLE
        } else {
            navView.visibility = View.GONE

        }
    }
}