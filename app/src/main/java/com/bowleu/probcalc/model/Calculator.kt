package com.bowleu.probcalc.model

import android.util.Log
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.PI
import kotlin.math.E

object Calculator {
    private const val TAG = "Calculator"

    private val precedence = mapOf("+" to 1, "-" to 1, "×" to 2, "÷" to 2, "^" to 3)
    private val unaryLeftFunctions = setOf("sin", "cos", "tan", "asin", "acos", "atan", "lg", "ln", "√")
    private val unaryRightFunctions = setOf("!")

    fun calculate(expression: String): String {
        val infixTokens = toPostfix(tokenize(expression)).toMutableList()
        var lastNumbers = 0
        var i = 0
        while (i < infixTokens.size) {
            when {
                infixTokens[i].matches("""\d+,?\d*|e|π""".toRegex()) -> {
                    if (infixTokens[i] == "e")
                        infixTokens[i] = E.toString()
                    else if (infixTokens[i] == "π")
                        infixTokens[i] = PI.toString()
                    lastNumbers++
                }
                infixTokens[i] in precedence.keys -> {
                    if (lastNumbers < 2)
                        throw IllegalStateException("Binary operator without two operands.")
                    val operator = infixTokens.removeAt(i)
                    val secondNumber = infixTokens.removeAt(i - 1).replace(",", ".").toDouble()
                    val firstNumber = infixTokens[i - 2].replace(",", ".").toDouble()
                    infixTokens[i - 2] = simpleCalculation(firstNumber, secondNumber, operator).toString()
                    lastNumbers -= 1
                    i -= 2
                }
                infixTokens[i] in unaryLeftFunctions + unaryRightFunctions -> {
                    if (lastNumbers < 1)
                        throw IllegalStateException("Unary operator without operand.")
                    val operator = infixTokens.removeAt(i)
                    val number = infixTokens[i - 1].replace(",", ".").toDouble()
                    infixTokens[i - 1] = simpleCalculation(number, operator).toString()
                    i -= 1
                }
            }
            Log.d(TAG, infixTokens.toString())
            i++
        }
        return infixTokens.last().replace(".", ",").run {
            if (this.endsWith(",0")) dropLast(2) else this
        }
    }

    fun calcComb(n: Int, k: Int): Int {
        return calcFact(n) / (calcFact(n - k) * calcFact(k))
    }

    fun calcAccomm(n: Int, k: Int): Int {
        return calcFact(n) / calcFact(n - k)
    }

    fun calcFact(x: Int): Int {
        require(x >= 0) {
            "Cannot be an negative number."
        }
        return if (x == 0) 1 else x * calcFact(x - 1)
    }

    fun calcCombWithReps(n: Int, k: Int): Int {
        return calcComb(n + k - 1, k)
    }

    fun calcBernoulli(n: Int, k: Int, q: Double): Double {
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

    private fun tokenize(expression: String): List<String> {
        val regex = """(\d+,?\d*|e|π|ln|lg|asin|acos|atan|sin|cos|tan|C\(\d+,\d+\)|A\(\d+,\d+\)|!C\(\d+,\d+\)|P\(\d+,\d+\)|\+|√|-|×|÷|\^|\(|\)|!)""".toRegex()
        return regex.findAll(expression.replace(" ", "")).map{it.value}.toList()
    }

    private fun toPostfix(expression: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = mutableListOf<String>()

        val rightAssociative = setOf("^")

        for (token in expression) {
            when {
                token.matches("""\d+,?\d*|e|π""".toRegex()) -> output.add(token) // Число
                token in precedence.keys -> {
                    while (stack.isNotEmpty() && stack.last() in precedence.keys && stack.last() != "!" &&
                        ((token !in rightAssociative && precedence[token]!! <= precedence[stack.last()]!!) ||
                                (token in rightAssociative && precedence[token]!! < precedence[stack.last()]!!))) {
                        output.add(stack.removeAt(stack.lastIndex))
                    }
                    stack.add(token)
                }
                token in unaryRightFunctions -> output.add(token)
                token == "(" -> stack.add(token)
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        output.add(stack.removeAt(stack.lastIndex))
                    }
                    if (stack.isNotEmpty() && stack.last() == "(") {
                        stack.removeAt(stack.lastIndex) // Удаляем "("
                        if (stack.isNotEmpty() && stack.last() in unaryLeftFunctions) {
                            output.add(stack.removeAt(stack.lastIndex))
                        }
                    }
                }
                else -> stack.add(token)
            }
        }
        while (stack.isNotEmpty()) {
            output.add(stack.removeAt(stack.lastIndex))
        }
        return output
    }

    private fun simpleCalculation(a: Double, b: Double, operator: String): Double {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> a / b
            "^" -> a.pow(b)
            else -> a
        }
    }

    private fun simpleCalculation(a: Double, function: String): Double {
        return when (function) {
            "sin" -> sin(a)
            "cos" -> cos(a)
            "tan" -> tan(a)
            "atan" -> atan(a)
            "asin" -> asin(a)
            "acos" -> acos(a)
            "√" -> sqrt(a)
            "lg" -> log10(a)
            "ln" -> ln(a)
            "!" -> calcFact(a.toInt()).toDouble()
            else -> a
        }
    }

}