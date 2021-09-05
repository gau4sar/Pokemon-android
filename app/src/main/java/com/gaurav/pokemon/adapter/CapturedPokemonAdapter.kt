package com.gaurav.pokemon.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.databinding.ItemCapturedBinding
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.GeneralUtils.getPokemonImageUrl
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

        Timber.d("onBindViewHolder")

        pokemonLocationInfoList.forEach {pokemonLocationInfo ->

            val url = getPokemonImageUrl(pokemonLocationInfo.id)

            Glide.with(fragmentActivity)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.ivPokemon)

            holder.binding.ivPokemon.setOnClickListener {
                val intent = Intent(fragmentActivity, PokemonDetailsActivity::class.java)
                val bundle = Bundle()

                bundle.putInt(Constants.POKEMON_ID, pokemonLocationInfo.id)
                bundle.putString(Constants.POKEMON_NAME, pokemonLocationInfo.name)
                bundle.putInt(Constants.POKEMON_STATUS, Constants.POKEMON_CAPTURED)

                intent.putExtras(bundle)
                startActivity(fragmentActivity, intent, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return pokemonLocationInfoList.size
    }
}