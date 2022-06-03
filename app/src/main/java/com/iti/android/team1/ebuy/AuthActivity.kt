package com.iti.android.team1.ebuy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.iti.android.team1.ebuy.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_activity_auth)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginScreen2, R.id.registerScreen2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}