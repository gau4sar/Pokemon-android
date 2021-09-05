package com.gaurav.pokemon.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.pokemon.data.model.MyTeam
import com.gaurav.pokemon.databinding.ItemMyTeamBinding
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.GeneralUtils.parseDateToShortMonthDateAndYear

class MyTeamAdapter(val list: List<MyTeam>, val fragmentActivity: FragmentActivity) :
    RecyclerView.Adapter<MyTeamAdapter.ViewHolder>() {

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
        holder.binding.tvName.text = list[position].name

        holder.binding.cvMyTeam.setOnClickListener {
            val intent = Intent(fragmentActivity, PokemonDetailsActivity::class.java)
            val bundle = Bundle()

            // TODO :: Send info to details page
            /*bundle.putSerializable(
                Constants.POKEMON_FOUND,
                PokemonFound(
                    list[position].id,
                    false,
                    LatLng(list[position].capturedLatAt, list[position].capturedLongAt),
                    false,
                    list[position].name
                )
            )*/

            intent.putExtras(bundle)
            ContextCompat.startActivity(fragmentActivity, intent, bundle)
        }

        holder.binding.tvCaptureAt.text = parseDateToShortMonthDateAndYear(list[position].capturedAt)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}