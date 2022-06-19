package com.vinithius.pokedexcodechallenge.extension

import android.net.Uri


fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar(Char::uppercase)
}

fun String.getIdIntoUrl(): String? {
    try {
        val parse = Uri.parse(this)
        parse.pathSegments.getOrNull(3)?.let {
            return it
        }
    } catch (e: Exception) {
        return null
    }
    return null
}
