package com.gaurav.pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.pokemon.databinding.ItemPokemonTypeBinding
import timber.log.Timber
import com.gaurav.pokemon.data.model.pokemon.Type
import com.gaurav.pokemon.utils.GeneralUtils.parseTypeToColor

class PokemonTypeAdapter(
    private val fragmentActivity: FragmentActivity,
    private val pokemonType: List<Type>
) :

    RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPokemonTypeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPokemonTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Timber.d("onBindViewHolder")

        pokemonType[position].let { pokemonType->
            holder.binding.llType.setBackgroundColor(parseTypeToColor(pokemonType).toInt())

            holder.binding.tvType.text = pokemonType.type.name
        }
    }

    override fun getItemCount(): Int {
        return pokemonType.size
    }
}