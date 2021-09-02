package com.gaurav.pokemon.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.databinding.ActivityMainBinding
import com.gaurav.pokemon.utils.BaseActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

/**
 *
 * MainActivity - The Launcher activity which is first loaded into the app
 *
 */
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_container)
    }
}