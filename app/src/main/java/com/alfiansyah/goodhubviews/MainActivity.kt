package com.alfiansyah.goodhubviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alfiansyah.goodhubviews.core.utils.avoidEdgeToEdge
import com.alfiansyah.goodhubviews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        avoidEdgeToEdge(binding)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        AppBarConfiguration(
            setOf(R.id.navigation_home,R.id.navigation_search)
        )
        binding.bottomNavigation.setupWithNavController(navController)
    }
}