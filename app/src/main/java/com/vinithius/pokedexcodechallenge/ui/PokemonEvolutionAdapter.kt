package com.vinithius.pokedexcodechallenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinithius.pokedexcodechallenge.R
import com.vinithius.pokedexcodechallenge.databinding.EvolutionViewholderBinding
import com.vinithius.pokedexcodechallenge.extension.capitalize

class PokemonEvolutionAdapter(
    private val pokemonEvolution: List<Pair<String, String>>
) : RecyclerView.Adapter<PokemonEvolutionAdapter.PokemonEvolutionViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: EvolutionViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonEvolutionViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.evolution_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonEvolutionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonEvolutionViewHolder, position: Int) {
        holder.bind(position, pokemonEvolution)
    }

    override fun getItemCount() = pokemonEvolution.size

    inner class PokemonEvolutionViewHolder(val binding: EvolutionViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            evolution_list: List<Pair<String, String>>
        ) {
            with(binding) {
                evolution_list[position].first.let {
                    setImage(it)
                    val arrowVisible = evolution_list.size == position + 1
                    textNamePokemon.text = it.capitalize()
                    arrowEvolution.isVisible = !arrowVisible
                }
            }
        }

        /**
         * Add the pokemon image from the source.
         */
        private fun setImage(name: String) {
            val urlImage = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
            Picasso.get()
                .load(urlImage)
                .error(R.drawable.ic_error_image)
                .into(binding.imagePokemon)
        }
    }

}
