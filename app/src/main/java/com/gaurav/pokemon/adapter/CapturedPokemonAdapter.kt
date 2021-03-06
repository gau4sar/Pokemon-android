package com.gaurav.pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.databinding.ItemCapturedBinding
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.GeneralUtils
import com.gaurav.pokemon.utils.GeneralUtils.getPokemonImageUrl
import com.gaurav.pokemon.utils.load
import timber.log.Timber

class CapturedPokemonAdapter(
    private val fragmentActivity: FragmentActivity,
    private val pokemonLocationInfoList: List<PokemonLocationInfo>
) :

    RecyclerView.Adapter<CapturedPokemonAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCapturedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCapturedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pokemonLocationInfo = pokemonLocationInfoList[position]

        holder.binding.apply {

            val url = getPokemonImageUrl(pokemonLocationInfo.id)

            ivPokemon.load(url,fragmentActivity,true)

            ivPokemon.setOnClickListener {

                Timber.d("ivPokemon setOnClickListener pokemonLocationInfo $pokemonLocationInfo")

                GeneralUtils.intentPokemonDetails(
                    fragmentActivity, pokemonLocationInfo, Constants.POKEMON_CAPTURED, "", it
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return pokemonLocationInfoList.size
    }
}