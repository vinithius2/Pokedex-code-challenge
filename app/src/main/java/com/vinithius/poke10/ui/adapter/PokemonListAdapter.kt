package com.vinithius.poke10.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.databinding.ViewholderPokemonBinding
import com.vinithius.poke10.datasource.response.Pokemon
import com.vinithius.poke10.extension.capitalize
import com.vinithius.poke10.extension.getIdIntoUrl
import com.vinithius.poke10.extension.setPokemonImage

class PokemonListAdapter : PagingDataAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>(
    COMPARATOR
) {

    var onCallBackClickDetail: ((id: Int) -> Unit)? = null
    var onCallBackClickFavorite: ((pokemon: Pokemon) -> Unit)? = null

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
            onCallBackClickDetail: ((id: Int) -> Unit)?
        ) {
            with(binding) {
                titlePokemon.text = pokemon.name.capitalize()
                layoutData.setOnClickListener {
                    pokemon.url?.getIdIntoUrl()?.let {
                        onCallBackClickDetail?.invoke(it.toInt())
                    }
                }
                with(imgPokeball) {
                    setData(pokemon.name)
                    setOnClickListener {
                        clickPokeball()
                        onCallBackClickFavorite?.invoke(pokemon)
                    }
                }
                setImage(pokemon)
            }
        }

        /**
         * Add the pokemon image from the source.
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
