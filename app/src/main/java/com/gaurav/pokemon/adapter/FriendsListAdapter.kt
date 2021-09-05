package com.gaurav.pokemon.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.databinding.ItemFriendBinding
import com.gaurav.pokemon.ui.main.screens.pokemon_details.PokemonDetailsActivity
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.GeneralUtils
import com.gaurav.pokemon.utils.getFormattedDateTime

/**
 * RecyclerView Adapter to handle data in FriendListFragment
 */

class FriendsListAdapter(val context: FragmentActivity) :
    RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * DiffUtil implementation for better rendering of RecyclerView
     */
    private val differCallback = object : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(
            oldItem: Friend,
            newItem: Friend
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Friend,
            newItem: Friend
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {

        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return FriendsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {

        val friend = differ.currentList[position]

        val pokemon = friend.pokemon

        holder.binding.apply {

            tvName.text = friend.name
            tvCapturedAt.text = " : ${getFormattedDateTime(pokemon.capturedAt)}"

            val imageUrl = GeneralUtils.getPokemonImageUrl(pokemon.id)

            Glide.with(context)
                .load(imageUrl)
                .into(ivPokemon)

            cardView.setOnClickListener {
                val intent = Intent(context, PokemonDetailsActivity::class.java)
                val bundle = Bundle()

                bundle.putInt(Constants.POKEMON_ID, pokemon.id)
                bundle.putString(Constants.POKEMON_NAME, pokemon.name)
                bundle.putInt(Constants.POKEMON_STATUS, Constants.POKEMON_CAPTURED)

                intent.putExtras(bundle)
                ContextCompat.startActivity(context, intent, bundle)
            }
        }
    }

    /**
     * Click listener handler for list items
     */

    private var onItemClickListener: ((Friend) -> Unit)? = null

    fun setOnItemClickListener(listener: (Friend) -> Unit) {
        onItemClickListener = listener
    }
}