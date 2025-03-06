package com.bowleu.probcalc.util

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
        ',' -> true
        else -> this.isLetter()
    }
}

fun Char.isClosingSymbol(): Boolean {
    return when (this) {
        ')' -> true
        '!' -> true
        ',' -> true
        else -> false
    }
}