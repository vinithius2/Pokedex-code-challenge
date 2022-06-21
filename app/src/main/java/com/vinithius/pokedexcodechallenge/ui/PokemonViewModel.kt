package com.vinithius.pokedexcodechallenge.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vinithius.pokedexcodechallenge.datasource.repository.PokemonRepository
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    var currentResult: LiveData<PagingData<Pokemon>>? = null

    /**
     * Get pokemons list using Paging3.
     */
    fun getPokemonList(): LiveData<PagingData<Pokemon>>? {
        try {
            currentResult = repository.getPokemonList().cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.e("Error list pokemons", e.toString())
        }
        return currentResult
    }

    fun setFavorite(pokemon: Pokemon, context: Context?) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                repository.setFavorite(pokemon, context)
            }
        } catch (e: Exception) {
            Log.e("setFavorite", e.toString())
        }
    }

}