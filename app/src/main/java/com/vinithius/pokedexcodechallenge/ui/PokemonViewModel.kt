package com.vinithius.pokedexcodechallenge.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _pokemonDetail = MutableLiveData<Pokemon?>()
    val pokemonDetail: LiveData<Pokemon?>
        get() = _pokemonDetail

    private val _pokemonDetailLoading = MutableLiveData<Boolean>()
    val pokemonDetailLoading: LiveData<Boolean>
        get() = _pokemonDetailLoading

    private val _pokemonDetailError = MutableLiveData<Boolean>()
    val pokemonDetailError: LiveData<Boolean>
        get() = _pokemonDetailError

    private var _idPokemon: Int = 0

    fun setIdPokemon(value: Int) {
        _idPokemon = value
    }

    fun cleanPokemon() {
        _pokemonDetail.value = null
    }

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

    /**
     * Set favorite pokemon to webhook.
     */
    fun setFavorite(pokemon: Pokemon, context: Context?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.setFavorite(pokemon, context)
            } catch (e: Exception) {
                Log.e("setFavorite", e.toString())
            }
        }
    }

    /**
     * Get all details pokemon.
     */
    fun getPokemonDetail() {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonDetailError.postValue(false)
            _pokemonDetailLoading.postValue(true)
            try {
                val pokemon = repository.getPokemonDetail(_idPokemon)
                _pokemonDetail.postValue(pokemon)
            } catch (e: Exception) {
                _pokemonDetailError.postValue(true)
                Log.e("getPokemon", e.toString())
            }
            _pokemonDetailLoading.postValue(false)
        }
    }

}