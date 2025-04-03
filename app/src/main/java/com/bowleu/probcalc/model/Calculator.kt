package com.bowleu.probcalc.model

import android.util.Log
import com.bowleu.probcalc.util.isNumber
import com.bowleu.probcalc.util.round
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object Calculator {
    private const val TAG = "Calculator"
    private val precedence = mapOf(
        "+" to 1,
        "-" to 1,
        "×" to 2,
        "÷" to 2,
        "^" to 3,
        "log" to 4,
        "¬C" to 4,
        "C" to 4,
        "A" to 4
    )
    private val leftFunctions =
        setOf("sin", "cos", "tan", "asin", "acos", "atan", "lg", "ln", "√", "¬C", "ceil", "floor")
    private val rightFunctions = setOf("!")

    fun calculate(expression: String): String {
        val infixTokens = toPostfix(tokenize(expression)).toMutableList()
        var i = 0
        while (i < infixTokens.size) {
            when {
                infixTokens[i].isNumber() -> {
                    if (infixTokens[i] == "e")
                        infixTokens[i] = E.toString() // Заменяем e, pi на соответствующие числа
                    else if (infixTokens[i] == "π")
                        infixTokens[i] = PI.toString()
                }

                infixTokens[i] in precedence.keys -> {
                    Log.d(TAG, infixTokens.toString())
                    if (!infixTokens[i - 1].isNumber() || !infixTokens[i - 2].isNumber())
                        throw IllegalStateException("Binary operator without two operands.")
                    val operator = infixTokens.removeAt(i)
                    val secondNumber = infixTokens.removeAt(i - 1).replace(",", ".").toDouble()
                    val firstNumber = infixTokens[i - 2].replace(",", ".").toDouble()
                    infixTokens[i - 2] =
                        simpleCalculation(firstNumber, secondNumber, operator).toString()
                    i -= 2
                    Log.d(TAG, infixTokens.toString())
                }

                infixTokens[i] in leftFunctions + rightFunctions -> {
                    if (!infixTokens[i - 1].isNumber())
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
        val answer = infixTokens.last().toDouble().round(8).toString()
        return answer.replace(".", ",").run {
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
        val regex =
            """(\d+,?\d*|\((.*),(.*)\)|ceil|floor|e|π|ln|lg|log|P|A|¬C|C|asin|acos|atan|sin|cos|tan|\+|√|-|×|÷|\^|\(|\)|!)""".toRegex()
        return regex.findAll(expression.replace(" ", "")).map { it.value }.toList()
    }

    private fun toPostfix(expression: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = mutableListOf<String>()

        val rightAssociative = setOf("^")
        for (token in expression) {
            when {
                token.matches("""\((.*),(.*)\)""".toRegex()) -> {
                    val matchResult = """\((.*),(.*)\)""".toRegex().find(token)
                    if (matchResult != null) {
                        output += toPostfix(tokenize(matchResult.groupValues[1]))
                        output += toPostfix(tokenize(matchResult.groupValues[2]))
                        Log.d(TAG, "First arg: ${toPostfix(tokenize(matchResult.groupValues[1]))}")
                        Log.d(TAG, "Second arg: ${toPostfix(tokenize(matchResult.groupValues[2]))}")
                        Log.d(TAG, output.toString())
                    }
                } // Рассматриваем такую функцию как число
                token.matches("""\d+,?\d*|e|π""".toRegex()) -> output.add(token) // Число
                token in precedence.keys -> {
                    while (stack.isNotEmpty() && stack.last() in precedence.keys && stack.last() != "!" &&
                        ((token !in rightAssociative && precedence[token]!! <= precedence[stack.last()]!!) ||
                                (token in rightAssociative && precedence[token]!! < precedence[stack.last()]!!))
                    ) {
                        output.add(stack.removeAt(stack.lastIndex))
                    }
                    stack.add(token)
                }

                token in rightFunctions -> output.add(token) // Рассматриваем такую функцию как число
                token == "(" -> stack.add(token)
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        output.add(stack.removeAt(stack.lastIndex))
                    }
                    if (stack.isNotEmpty() && stack.last() == "(") {
                        stack.removeAt(stack.lastIndex) // Удаляем "("
                        if (stack.isNotEmpty() && stack.last() in leftFunctions) {
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

    fun simpleCalculation(a: Double, b: Double, operator: String): Double {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> a / b
            "^" -> a.pow(b)
            "log" -> log(b, a)
            "C" -> calcComb(a.toInt(), b.toInt()).toDouble()
            "A" -> calcAccomm(a.toInt(), b.toInt()).toDouble()
            "¬C" -> calcCombWithReps(a.toInt(), b.toInt()).toDouble()
            else -> a
        }
    }

    fun simpleCalculation(a: Double, function: String): Double {
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
            "ceil" -> ceil(a)
            "floor" -> floor(a)
            else -> a
        }
    }

}