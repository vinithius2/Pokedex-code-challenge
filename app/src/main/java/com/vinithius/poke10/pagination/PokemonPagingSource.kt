package com.vinithius.poke10.pagination

import androidx.paging.PagingSource
import com.vinithius.poke10.datasource.repository.PokemonRemoteDataSource

import androidx.paging.PagingState
import com.vinithius.poke10.datasource.response.Pokemon
import retrofit2.HttpException
import java.io.IOException

class PokemonPagingSource(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource
) : PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val pageNumber = params.key ?: 0
        return try {
            val response = pokemonRemoteDataSource.getPokemonList(pageNumber)
            val pokemons = response.results
            val prevKey = if (pageNumber > 0) pageNumber - OFFSET else null
            val nextKey = if (response.results.isNotEmpty()) pageNumber + OFFSET else null
            LoadResult.Page(data = pokemons, prevKey = prevKey, nextKey = nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        TODO("Not yet implemented")
    }

    companion object {
        const val OFFSET = 20
    }

}
