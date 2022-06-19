package com.vinithius.pokedexcodechallenge.datasource.repository

import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import com.vinithius.pokedexcodechallenge.datasource.response.PokemonDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonRemoteDataSource {

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int
    ): PokemonDataWrapper

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): Pokemon

}