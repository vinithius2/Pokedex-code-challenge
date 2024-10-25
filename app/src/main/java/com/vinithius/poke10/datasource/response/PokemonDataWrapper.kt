package com.vinithius.poke10.datasource.response

data class PokemonDataWrapper(
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: List<Pokemon>
)
