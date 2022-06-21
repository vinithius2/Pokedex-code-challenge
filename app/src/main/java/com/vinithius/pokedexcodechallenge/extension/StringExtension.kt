package com.vinithius.pokedexcodechallenge.extension

import android.content.Context
import android.net.Uri
import com.vinithius.pokedexcodechallenge.ui.PokemonListAdapter


fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar(Char::uppercase)
}

fun String.getIdIntoUrl(): String? {
    try {
        val parse = Uri.parse(this)
        parse.pathSegments.getOrNull(3)?.let {
            return it
        }
    } catch (e: Exception) {
        return null
    }
    return null
}

/**
 * Get if the pokemon is favorite or not, if null, returns false.
 */
fun String.getIsFavorite(context : Context): Boolean {
    val sharedPref = context.getSharedPreferences(
        PokemonListAdapter.FAVORITES,
        Context.MODE_PRIVATE
    )
    return sharedPref.getBoolean(this, false)
}
