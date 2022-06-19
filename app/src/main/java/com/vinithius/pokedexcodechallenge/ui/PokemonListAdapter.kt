package com.vinithius.pokedexcodechallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.pokedexcodechallenge.databinding.ViewholderPokemonBinding
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon

class PokemonListAdapter() :
    PagingDataAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>(COMPARATOR) {

    var onCallBackClickDetail: ((id: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ViewholderPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PokemonViewHolder(private val binding: ViewholderPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) {
            with(binding) {
                title.text = pokemon.name
            }
        }
    }

    companion object {

        private object COMPARATOR : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }

    }

}
