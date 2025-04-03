package com.bowleu.probcalc.util

import kotlin.math.round

fun Char.isOperator(): Boolean {
    return when (this) {
        '+' -> true
        '-' -> true
        '÷' -> true
        '×' -> true
        '^' -> true
        else -> false
    }
}

fun Char.isOpeningSymbol(): Boolean {
    return when (this) {
        '(' -> true
        '√' -> true
        else -> false
    }
}

fun Char.isClosingSymbol(): Boolean {
    return when (this) {
        ')' -> true
        '!' -> true
        else -> false
    }
}


fun String.lastChar(): Char {

    return if (lastIndex > -1) {
        this[lastIndex]
    } else {
        ' '
    }
}

fun String.isNumber(): Boolean {
    return this.matches("""\d+,?\d*|\d+\.?\d*|e|π""".toRegex())
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}