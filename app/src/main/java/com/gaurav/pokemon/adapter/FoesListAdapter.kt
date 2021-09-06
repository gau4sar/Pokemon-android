package com.gaurav.pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.Foe
import com.gaurav.pokemon.databinding.ItemFriendBinding
import com.gaurav.pokemon.adapter.FoesListAdapter.FoesViewHolder
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED
import com.gaurav.pokemon.utils.Constants.POKEMON_CAPTURED_BY_OTHER
import com.gaurav.pokemon.utils.GeneralUtils
import com.gaurav.pokemon.utils.getFormattedDateTime

class FoesListAdapter(val context: FragmentActivity) :
    RecyclerView.Adapter<FoesViewHolder>() {

    inner class FoesViewHolder(val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * DiffUtil implementation for better rendering of RecyclerView
     */
    private val differCallback = object : DiffUtil.ItemCallback<Foe>() {
        override fun areItemsTheSame(
            oldItem: Foe,
            newItem: Foe
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Foe,
            newItem: Foe
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    /**
     * RecyclerView Adapter callback methods
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoesViewHolder {

        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return FoesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoesViewHolder, position: Int) {

        val foe = differ.currentList[position]

        val pokemon = foe.pokemon

        holder.binding.apply {
            tvName.text = foe.name
            tvCapturedAt.text = getFormattedDateTime(pokemon.capturedAt)

            val imageUrl = GeneralUtils.getPokemonImageUrl(pokemon.id)

            Glide.with(context)
                .load(imageUrl)
                .into(ivPokemon)

            val pokemonLocationInfo = PokemonLocationInfo(
                pokemon.capturedAt, 0.00, 0.00,
                pokemon.id, pokemon.name
            )

            clMain.background = ContextCompat.getDrawable(context, R.color.veryLightRed)

            cardView.setOnClickListener {
                GeneralUtils.intentPokemonDetails(
                    context, pokemonLocationInfo, POKEMON_CAPTURED_BY_OTHER, foe.name
                )
            }
        }
    }
}