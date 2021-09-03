package com.gaurav.pokemon.ui.main.screens.pokemon_details

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.databinding.ActivityPokemonDetailsBinding
import com.gaurav.pokemon.utils.BaseActivity
import timber.log.Timber

class PokemonDetailsActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityPokemonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity")

        binding = ActivityPokemonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_pokemon_fragment)
    }
}