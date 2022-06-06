package com.iti.android.team1.ebuy

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.connection.ConnectionLiveData
import com.iti.android.team1.ebuy.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding
    private lateinit var fragementConatiner: FragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        fragementConatiner = findViewById(R.id.nav_host_fragment_activity_auth)
        navController = findNavController(R.id.nav_host_fragment_activity_auth)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        handleConnection()
    }

    private fun handleConnection() {
        ConnectionLiveData(this).observe(this) { connection ->
            if (connection) {
                handleIsConnected()
            } else {
                handleNotConnected()
            }
        }
    }

    private fun handleIsConnected() {
        binding.noConnection.root.visibility = View.INVISIBLE
        fragementConatiner.visibility = View.VISIBLE
        binding.appBarLayout.visibility = View.VISIBLE
        showSnackBar(getString(R.string.connected))

    }

    private fun handleNotConnected() {
        fragementConatiner.visibility = View.INVISIBLE
        binding.appBarLayout.visibility = View.INVISIBLE
        binding.noConnection.root.visibility = View.VISIBLE
        showSnackBar(getString(R.string.not_connected))
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.DKGRAY)
//            .setBackgroundTint(resources.getColor(R.color.backgroundColor))
            .show()
    }
}