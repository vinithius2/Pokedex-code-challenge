package com.vinithius.pokedexcodechallenge.components

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.vinithius.pokedexcodechallenge.R
import com.vinithius.pokedexcodechallenge.databinding.PokeballComponentBinding
import com.vinithius.pokedexcodechallenge.extension.getIsFavorite
import com.vinithius.pokedexcodechallenge.ui.PokemonListAdapter

class PokeballComponent(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var pokemon_name: String = ""
        set(value) {
            field = value
            getStatusImagePokeball(value)
        }

    private val binding = PokeballComponentBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * Changes the item's Pokeball image according to the status of favorites.
     */
    private fun getStatusImagePokeball(name: String) {
        val isFavorite = name.getIsFavorite(context)
        if (isFavorite) {
            binding.imagePokeball.background =
                ContextCompat.getDrawable(binding.root.context, R.drawable.pokeball_01)
        } else {
            binding.imagePokeball.background =
                ContextCompat.getDrawable(binding.root.context, R.drawable.pokeball_03_gray)
        }
    }

    /**
     * Adds the boolean values of the favorite.
     */
    private fun setPreferences(name: String, value: Boolean) {
        val sharedPref =
            context.getSharedPreferences(PokemonListAdapter.FAVORITES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(name, value)
            commit()
        }
    }

    /**
     * Makes the Pokeball anim opening or closing.
     */
    private fun setAnimation(id_click: Int) {
        binding.imagePokeball.background =
            ContextCompat.getDrawable(context, id_click)
        val frameAnimation: AnimationDrawable =
            binding.imagePokeball.background as AnimationDrawable
        frameAnimation.start()
    }

    /**
     * Pokeball click callback to add as favorite or not.
     */
    fun clickPokeball(): Boolean {
        val isFavorite = pokemon_name.getIsFavorite(context)
        setPreferences(pokemon_name, !isFavorite)
        val draw = if (isFavorite) R.drawable.animation_click_off else R.drawable.animation_click_on
        setAnimation(draw)
        return isFavorite
    }

    fun setData(
        name: String
    ) {
        pokemon_name = name
    }

}
