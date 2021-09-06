package com.gaurav.pokemon.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.pokemon.data.model.MyTeam
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.databinding.ItemMyTeamBinding
import com.gaurav.pokemon.ui.main.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.GeneralUtils
import com.gaurav.pokemon.utils.GeneralUtils.getPokemonImageUrl
import com.gaurav.pokemon.utils.GeneralUtils.parseDateToShortMonthDateAndYear
import com.gaurav.pokemon.utils.load

class MyTeamAdapter(
    val myTeamList: List<MyTeam>,
    private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<MyTeamAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMyTeamBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyTeamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pokemon = myTeamList[position]

        holder.binding.apply {

            tvName.text = pokemon.name

            cvMyTeam.setOnClickListener {

                val pokemonLocationInfo = PokemonLocationInfo(
                    pokemon.capturedAt, pokemon.capturedLatAt, pokemon.capturedLongAt,
                    pokemon.id, pokemon.name
                )

                GeneralUtils.intentPokemonDetails(
                    fragmentActivity, pokemonLocationInfo, Constants.POKEMON_CAPTURED, ""
                )
            }

            tvCaptureAt.text =
                parseDateToShortMonthDateAndYear(pokemon.capturedAt)

            ivPokemon.load(getPokemonImageUrl(pokemon.id),fragmentActivity)

        }
    }

    override fun getItemCount(): Int {
        return myTeamList.size
    }
}