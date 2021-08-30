package com.gaurav.pokemon.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 *
 * MainActivity - The Launcher activity which is first loaded into the app
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_container)

        mainViewModel = getViewModel()
    }
}