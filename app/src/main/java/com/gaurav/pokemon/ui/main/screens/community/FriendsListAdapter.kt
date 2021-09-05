package com.gaurav.pokemon.ui.main.screens.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.pokemon.data.model.Friend
import com.gaurav.pokemon.databinding.ItemFriendBinding
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

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {

        val friend = differ.currentList[position]

        holder.binding.apply {
            tvName.text = "${friend.name}"
            tvCapturedAt.text = " : ${getFormattedDateTime(friend.pokemonCapturedInfo.capturedAt)}"

            val imageUrl = ""
            Glide.with(context)
                .load(imageUrl)
                .into(ivPokemon)

            cardView.setOnClickListener {
                //TODO :: Go to details page
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