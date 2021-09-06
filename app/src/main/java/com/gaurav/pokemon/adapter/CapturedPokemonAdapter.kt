package com.gaurav.pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.databinding.ItemCapturedBinding
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.GeneralUtils
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

        pokemonLocationInfoList.forEach { pokemonLocationInfo ->

            val url = getPokemonImageUrl(pokemonLocationInfo.id)

            Glide.with(fragmentActivity)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.ivPokemon)

            holder.binding.ivPokemon.setOnClickListener {

                Timber.d("ivPokemon setOnClickListener")

                GeneralUtils.intentPokemonDetails(
                    fragmentActivity,pokemonLocationInfo, Constants.POKEMON_CAPTURED,"")
            }
        }
    }

    override fun getItemCount(): Int {
        return pokemonLocationInfoList.size
    }
}