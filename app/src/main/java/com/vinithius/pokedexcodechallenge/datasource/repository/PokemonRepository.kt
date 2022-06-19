package com.vinithius.pokedexcodechallenge.datasource.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vinithius.pokedexcodechallenge.pagination.PokemonPagingSource

class PokemonRepository(private val remoteDataSource: PokemonRemoteDataSource) {

    fun getPokemonList() =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PokemonPagingSource(remoteDataSource)
            }
        ).liveData

}