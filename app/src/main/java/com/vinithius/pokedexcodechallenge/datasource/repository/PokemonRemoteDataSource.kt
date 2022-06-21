package com.vinithius.pokedexcodechallenge.datasource.repository

import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import com.vinithius.pokedexcodechallenge.datasource.response.PokemonDataWrapper
import retrofit2.http.*

interface PokemonRemoteDataSource {

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int
    ): PokemonDataWrapper

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): Pokemon

    @POST
    suspend fun setFavorite(
        @Url url: String,
        @Body pokemon: Pokemon
    )

    @HTTP(
        method = "DELETE",
        hasBody = true
    )
    suspend fun deleteFavorite(@Url url: String, @Body pokemon: Pokemon)

}