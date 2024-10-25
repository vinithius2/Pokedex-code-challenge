package com.vinithius.poke10.extension

fun Int.converterIntToDouble(): Double {
    return this.toDouble().div(10)
}

fun Int.convertPounds(): Double {
    val value = this.toDouble().div(10)
    return value.times(2.20462262)
}

fun Int.convertInch(): Double {
    val value = this.toDouble().div(10)
    return 39.370 * value
}