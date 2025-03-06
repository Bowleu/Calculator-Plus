package com.bowleu.probcalc

import java.lang.Math.pow
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.tan

object Calculator {

    fun calculate(expression: String): Double {
        TODO("Not yet implemented")
    }

    private fun calcComb(n: Int, k: Int): Int {
        return calcFact(n) / (calcFact(n - k) * calcFact(k))
    }

    private fun calcAccomm(n: Int, k: Int): Int {
        return calcFact(n) / calcFact(n - k)
    }

    private fun calcFact(x: Int): Int {
        require(x >= 0) {
            "Cannot be an negative number."
        }
        return if (x == 0) 1 else x * calcFact(x - 1)
    }

    private fun calcCombWithReps(n: Int, k: Int): Int {
        return calcComb(n + k - 1, k)
    }

    private fun calcBernoulli(n: Int, k: Int, q: Double): Double {
        val c = calcComb(n, k)
        val mn = min(k, (n - k))
        var prob = 1.0
        prob *= if (k < n - k) {
            (q - 1).pow(n - k - k)
        } else {
            q.pow(k + k - n)
        }
        prob *= (q * (q - 1)).pow(mn)
        return c * prob
    }

    private fun toInfix(expression: String): String {
        TODO("NOT YET IMPLEMENTED")
    }

}