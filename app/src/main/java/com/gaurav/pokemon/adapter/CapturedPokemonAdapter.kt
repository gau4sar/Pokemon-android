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
import com.gaurav.pokemon.data.model.PokemonFound
import com.gaurav.pokemon.databinding.ItemCapturedBinding
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.Constants
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

class CapturedPokemonAdapter(val fragmentActivity: FragmentActivity) :
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

        Glide.with(fragmentActivity)
            .load(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_barbasaur_front))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.binding.ivPokemon)

        holder.binding.ivPokemon.setOnClickListener {
            val intent = Intent(fragmentActivity, PokemonDetailsActivity::class.java)
            val bundle = Bundle()

            bundle.putSerializable(
                Constants.POKEMON_FOUND,
                PokemonFound(1, false, LatLng(27.5259, 95.5066), false)
            )

            intent.putExtras(bundle)
            startActivity(fragmentActivity, intent, bundle)
        }
    }

    override fun getItemCount(): Int {
        return 15
    }
}