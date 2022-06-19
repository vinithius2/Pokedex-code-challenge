package com.vinithius.pokedexcodechallenge.datasource.response

data class Pokemon(
    var id: Int?,
    var name: String,
    var url: String?,
    var height: Int?,
    var weight: Int?,
    var base_experience: Int?
)