package com.vinithius.pokedexcodechallenge.datasource.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vinithius.pokedexcodechallenge.BuildConfig
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import com.vinithius.pokedexcodechallenge.extension.getIsFavorite
import com.vinithius.pokedexcodechallenge.pagination.PokemonPagingSource


class PokemonRepository(private val remoteDataSource: PokemonRemoteDataSource) {

    fun getPokemonList() =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PokemonPagingSource(remoteDataSource)
            }
        ).liveData

    suspend fun setFavorite(pokemon: Pokemon, context: Context?) {
        context?.let {
            val favorite = pokemon.name.getIsFavorite(context)
            if (favorite) {
                remoteDataSource.setFavorite(urlFavorite, pokemon)
            } else {
                remoteDataSource.deleteFavorite(urlFavorite, pokemon)
            }
        }
    }

    companion object {
        const val urlFavorite: String = "https://webhook.site/${BuildConfig.WEBHOOK_KEY}"
    }

}