package com.vinithius.pokedexcodechallenge.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vinithius.pokedexcodechallenge.datasource.repository.PokemonRepository
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon

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

}