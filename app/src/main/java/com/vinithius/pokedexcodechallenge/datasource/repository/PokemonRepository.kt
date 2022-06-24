package com.vinithius.pokedexcodechallenge.datasource.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vinithius.pokedexcodechallenge.BuildConfig
import com.vinithius.pokedexcodechallenge.datasource.response.*
import com.vinithius.pokedexcodechallenge.extension.getIsFavorite
import com.vinithius.pokedexcodechallenge.pagination.PokemonPagingSource
import retrofit2.HttpException


class PokemonRepository(private val remoteDataSource: PokemonRemoteDataSource) {

    fun getPokemonList() =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PokemonPagingSource(remoteDataSource)
            }
        ).liveData

    suspend fun getPokemonDetail(id: Int): Pokemon? {
        return try {
            remoteDataSource.getPokemonDetail(id)
        } catch (e: HttpException) {
            Log.e("Pokemon (ID: $id) ", e.toString())
            null
        }
    }

    suspend fun getPokemonEncounters(id: Int): List<Location>? {
        return try {
            remoteDataSource.getPokemonEncounters(id)
        } catch (e: HttpException) {
            Log.e("Encounters (ID: $id) ", e.toString())
            null
        }
    }

    suspend fun getPokemonEvolution(id: Int): EvolutionChain? {
        return try {
            remoteDataSource.getPokemonEvolution(id)
        } catch (e: HttpException) {
            Log.e("EvolutionChain (ID: $id) ", e.toString())
            null
        }
    }

    suspend fun getPokemonCharacteristic(id: Int): Characteristic? {
        return try {
            remoteDataSource.getPokemonCharacteristic(id)
        } catch (e: HttpException) {
            Log.e("Characteristic (ID: $id) ", e.toString())
            null
        }
    }

    suspend fun getPokemonSpecies(id: Int): Specie? {
        return try {
            remoteDataSource.getPokemonSpecies(id)
        } catch (e: HttpException) {
            Log.e("Specie (ID: $id) ", e.toString())
            null
        }
    }

    suspend fun getPokemonDamageRelations(type: String): Damage? {
        return try {
            remoteDataSource.getPokemonDamageRelations(type)
        } catch (e: HttpException) {
            Log.e("Damage (Type: $type) ", e.toString())
            null
        }
    }

    suspend fun setFavorite(pokemon: Pokemon, context: Context?) {
        context?.let {
            try {
                val favorite = pokemon.name.getIsFavorite(context)
                if (favorite) {
                    remoteDataSource.setFavorite(urlFavorite, pokemon)
                } else {
                    remoteDataSource.deleteFavorite(urlFavorite, pokemon)
                }
            } catch (e: HttpException) {
                Log.e("Favorite", e.toString())
            }
        }
    }

    companion object {
        const val urlFavorite: String = "https://webhook.site/${BuildConfig.WEBHOOK_KEY}"
    }

}
