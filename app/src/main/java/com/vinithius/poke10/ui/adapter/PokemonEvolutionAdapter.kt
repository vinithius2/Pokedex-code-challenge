package com.vinithius.poke10.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.EvolutionViewholderBinding
import com.vinithius.poke10.extension.capitalize

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
            evolutionList: List<Pair<String, String>>
        ) {
            with(binding) {
                evolutionList[position].first.let {
                    setImage(it)
                    val arrowVisible = evolutionList.size == position + 1
                    textNamePokemon.text = it.capitalize()
                    arrowEvolution.isVisible = !arrowVisible
                }
            }
        }

        /**
         * Add the pokemon image from the external source, because we have just the name in this case.
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
