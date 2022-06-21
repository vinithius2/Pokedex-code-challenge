package com.vinithius.pokedexcodechallenge.datasource.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vinithius.pokedexcodechallenge.BuildConfig
import com.vinithius.pokedexcodechallenge.datasource.response.Damage
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

    suspend fun getPokemonDetail(id: Int): Pokemon {
        val pokemon = remoteDataSource.getPokemonDetail(id)
        val encountersRequest = remoteDataSource.getPokemonEncounters(id)
        val characteristicRequest = remoteDataSource.getPokemonCharacteristic(id)
        val specieRequest = remoteDataSource.getPokemonSpecies(id)
        Uri.parse(specieRequest.evolution_chain.url).pathSegments.getOrNull(3)?.let {
            remoteDataSource.getPokemonEvolution(it.toInt()).let { evolution_response ->
                pokemon.apply { evolution = evolution_response }
            }
        }
        val damageList: MutableList<Damage> = mutableListOf()
        pokemon.types?.forEach { type_list ->
            remoteDataSource.getPokemonDamageRelations(type_list.type.name).let {
                it.type = type_list.type
                damageList.add(it)
            }
        }
        pokemon.apply {
            encounters = encountersRequest
            characteristic = characteristicRequest
            specie = specieRequest
            damage = damageList.toList()
        }
        return pokemon
    }

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