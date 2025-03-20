package com.bowleu.probcalc.viewmodel

import androidx.lifecycle.ViewModel
import com.bowleu.probcalc.model.Calculator
import com.bowleu.probcalc.util.isClosingSymbol
import com.bowleu.probcalc.util.isOpeningSymbol
import com.bowleu.probcalc.util.isOperator
import com.bowleu.probcalc.util.lastChar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {
    private val calculator = Calculator
    private val _expression = MutableStateFlow("")
    private val _result = MutableStateFlow("")
    val expression = _expression.asStateFlow()
    val result = _result.asStateFlow()

    fun addToExpression(value: String) {
        if (value.isEmpty())
            return
        if (value[0].isOperator()) {
            if (_expression.value.isEmpty() || _expression.value.lastChar().isOpeningSymbol()) {
                if (value == "-") {
                    _expression.value += "0$value"
                }
            } else if (_expression.value.lastChar().isDigit() ||
                _expression.value.lastChar().isClosingSymbol() ||
                _expression.value.lastChar() == 'e' ||
                _expression.value.lastChar() == 'π') {
                _expression.value += value
            }
        } else if (value[0].isDigit()) {
            if (_expression.value.isEmpty()
                || !_expression.value.lastChar().isClosingSymbol()
                && !_expression.value.lastChar().isLetter()) {
                _expression.value += value
            }
        } else if (value == "e" || value == "π") {
            if (_expression.value.isEmpty()
                || !_expression.value.lastChar().isClosingSymbol()
                && !_expression.value.lastChar().isLetter()) {
                if (_expression.value.lastChar().isDigit())
                    _expression.value += "×"
                _expression.value += value
            }
        } else if (value == ",") {
            if (_expression.value.isEmpty()) {
                _expression.value += "0,"
            } else if (_expression.value.lastChar().isDigit()) {
                _expression.value += ','
            }
        } else if (value[0].isClosingSymbol()) {
            if (_expression.value.isNotEmpty() &&
                (_expression.value.lastChar().isDigit() ||
                        _expression.value.lastChar().isClosingSymbol() ||
                        _expression.value.lastChar() == 'e' ||
                        _expression.value.lastChar() == 'π')) {
                _expression.value += value
            }
        } else {
            if (_expression.value.isEmpty() ||
                _expression.value.lastChar().isOperator() ||
                _expression.value.lastChar().isOpeningSymbol()) {
                _expression.value += value
            }
        }
    }

    fun makeResultBeExpression() {
        _expression.value = _result.value
    }

    fun calculate() {
        _result.value = calculator.calculate(_expression.value)
    }

    fun clearExpression() {
        _expression.value = ""
    }

    fun dropLastExpressionSymbol() {
        _expression.value = _expression.value.dropLast(1)
    }

    fun clearResult() {
        _result.value = ""
    }
}