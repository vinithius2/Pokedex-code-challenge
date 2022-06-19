package com.vinithius.pokedexcodechallenge.di

import com.vinithius.pokedexcodechallenge.datasource.repository.PokemonRemoteDataSource
import com.vinithius.pokedexcodechallenge.datasource.repository.PokemonRepository
import com.vinithius.pokedexcodechallenge.ui.PokemonViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val repositoryModule = module {
    single { get<Retrofit>().create(PokemonRemoteDataSource::class.java) }
}

val repositoryDataModule = module {
    single { PokemonRepository(get()) }
}

val viewModelModule = module {
    single { PokemonViewModel(get()) }
}

val networkModule = module {
    single { retrofit() }
}

fun retrofit(): Retrofit {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        .build()

    return Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}