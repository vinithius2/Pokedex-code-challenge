package com.vinithius.pokedexcodechallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.pokedexcodechallenge.databinding.ViewholderPokemonBinding
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import com.vinithius.pokedexcodechallenge.extension.capitalize
import com.vinithius.pokedexcodechallenge.extension.setPokemonImage

class PokemonListAdapter :
    PagingDataAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>(COMPARATOR) {

    var onCallBackClickDetail: ((url: String) -> Unit)? = null

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
            holder.bind(currentItem, onCallBackClickDetail)
        }
    }

    inner class PokemonViewHolder(private val binding: ViewholderPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            pokemon: Pokemon,
            onCallBackClickDetail: ((url: String) -> Unit)?
        ) {
            with(binding) {
                titlePokemon.text = pokemon.name.capitalize()
                layoutData.setOnClickListener {
                    pokemon.url?.let { url -> onCallBackClickDetail?.invoke(url) }
                }
                with(imgPokeball) {
                    setData(pokemon.name)
                    setOnClickListener {
                        clickPokeball()
                    }
                }
                setImage(pokemon)
            }
        }

        /**
         * Add the pokemon image from the source "img.pokemondb" to each item in the list.
         */
        private fun setImage(pokemon: Pokemon) {
            binding.imagePokemon.setPokemonImage(pokemon)
        }

    }

    companion object {

        const val FAVORITES = "FAVORITES"

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
