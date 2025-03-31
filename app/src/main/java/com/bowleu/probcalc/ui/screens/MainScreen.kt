package com.bowleu.probcalc.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowleu.probcalc.R
import com.bowleu.probcalc.ui.theme.DarkGray
import com.bowleu.probcalc.ui.theme.LightGray
import com.bowleu.probcalc.ui.theme.MediumGray
import com.bowleu.probcalc.ui.theme.Orange
import com.bowleu.probcalc.util.LimitedList
import com.bowleu.probcalc.util.isClosingSymbol
import com.bowleu.probcalc.viewmodel.CalculatorViewModel

@Composable
fun MainScreen(viewModel: CalculatorViewModel) {
    var areAllVisible by remember { mutableStateOf(false) }
    val previousExpressions = remember { LimitedList<String>(3) }
    var isExpressionActive by remember { mutableStateOf(true) }
    var isSecond by remember { mutableStateOf(false) }

    val expression by viewModel.expression.collectAsState()
    val result by viewModel.result.collectAsState()

    val onInputButtonClicked = {
        if (!isExpressionActive) {
            previousExpressions.add("$expression = $result")
            viewModel.makeResultBeExpression()
            isExpressionActive = true
        }
    }

    Column(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                previousExpressions.get(0).let {
                    if (it != null)
                        PreviousExpression(it)
                }
                previousExpressions.get(1).let {
                    if (it != null)
                        PreviousExpression(it)
                }
                previousExpressions.get(2).let {
                    if (it != null)
                        PreviousExpression(it)
                }
            }
            MathLine(expression, isExpressionActive)
            MathLine(result, !isExpressionActive)
        }
        Column(modifier = Modifier.weight(0.6f)) {
            val equalWeight = Modifier.weight(1f)
            val equalWeightRow = Modifier
                .padding(0.dp, 15.dp)
                .weight(1f)
            if (areAllVisible) {
                ButtonsRow(equalWeightRow) {
                    KeyboardButton(
                        onClick = {
                            viewModel.addToExpression("C(n, k)")
                        },
                        modifier = equalWeight
                    ) {
                        ButtonText("C(n, k)", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("A(n, k)")
                    }, modifier = equalWeight) {
                        ButtonText("A(n, k)", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("P(n, k)")
                    }, modifier = equalWeight) {
                        ButtonText("P(n, k)", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("¬С(n, k)")
                    }, modifier = equalWeight) {
                        ButtonText("¬С(n, k)", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        //viewModel.addToExpression("∑")
                    }, modifier = equalWeight) {
                        ButtonText("", LightGray)
                    }
                }
                ButtonsRow(equalWeightRow) {
                    KeyboardButton(onClick = {
                        isSecond = !isSecond
                    }, modifier = equalWeight) {
                        ButtonText("2nd", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("log(,)")
                    }, modifier = equalWeight) {
                        ButtonText("log(n, k)", LightGray)
                    }
                    if (isSecond) {
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("asin(")
                        }, modifier = equalWeight) {
                            ButtonText("sin", LightGray, "-1")
                        }
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("acos(")
                        }, modifier = equalWeight) {
                            ButtonText("cos", LightGray, "-1")
                        }
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("atan(")
                        }, modifier = equalWeight) {
                            ButtonText("tan", LightGray, "-1")
                        }
                    } else {
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("sin(")
                        }, modifier = equalWeight) {
                            ButtonText("sin", LightGray)
                        }
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("cos(")
                        }, modifier = equalWeight) {
                            ButtonText("cos", LightGray)
                        }
                        KeyboardButton(onClick = {
                            onInputButtonClicked()
                            viewModel.addToExpression("tan(")
                        }, modifier = equalWeight) {
                            ButtonText("tan", LightGray)
                        }
                    }
                }
                ButtonsRow(equalWeightRow) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("^")
                    }, modifier = equalWeight) {
                        ButtonText("x^y", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("lg(")
                    }, modifier = equalWeight) {
                        ButtonText("lg", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("ln(")
                    }, modifier = equalWeight) {
                        ButtonText("ln", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("(")
                    }, modifier = equalWeight) {
                        ButtonText("(", LightGray)
                    }
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression(")")
                    }, modifier = equalWeight) {
                        ButtonText(")", LightGray)
                    }
                }
            }
            ButtonsRow(equalWeightRow) {
                if (areAllVisible) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("√(")
                    }, modifier = equalWeight) {
                        ButtonText("√", LightGray)
                    }
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    if (expression.isNotEmpty()) {
                        viewModel.clearExpression()
                    } else {
                        viewModel.clearResult()
                        previousExpressions.clear()
                    }

                }, modifier = equalWeight) {
                    ButtonText("AC", Orange)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.dropLastExpressionSymbol()
                }, modifier = equalWeight) {
                    Image(
                        painter = painterResource(id = R.drawable.delete_last),
                        contentDescription = "Delete last symbol",
                        colorFilter = ColorFilter.tint(Orange),
                        modifier = Modifier.height(20.dp)
                    )
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("%")
                }, modifier = equalWeight) {
                    ButtonText("%", Orange)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("÷")
                }, modifier = equalWeight) {
                    ButtonText("÷", Orange)
                }
            }
            ButtonsRow(equalWeightRow) {
                if (areAllVisible) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("!")
                    }, modifier = equalWeight) {
                        ButtonText("x!", LightGray)
                    }
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("7")
                }, modifier = equalWeight) {
                    ButtonText("7", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("8")
                }, modifier = equalWeight) {
                    ButtonText("8", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("9")
                }, modifier = equalWeight) {
                    ButtonText("9", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("×")
                }, modifier = equalWeight) {
                    ButtonText("×", Orange)
                }
            }
            ButtonsRow(equalWeightRow) {
                if (areAllVisible) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("×10^")
                    }, modifier = equalWeight) {
                        ButtonText("×10^x", LightGray)
                    }
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("4")
                }, modifier = equalWeight) {
                    ButtonText("4", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("5")
                }, modifier = equalWeight) {
                    ButtonText("5", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("6")
                }, modifier = equalWeight) {
                    ButtonText("6", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("-")
                }, modifier = equalWeight) {
                    ButtonText("-", Orange)
                }
            }
            ButtonsRow(equalWeightRow) {
                if (areAllVisible) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("π")
                    }, modifier = equalWeight) {
                        ButtonText("π", LightGray)
                    }
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("1")
                }, modifier = equalWeight) {
                    ButtonText("1", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("2")
                }, modifier = equalWeight) {
                    ButtonText("2", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("3")
                }, modifier = equalWeight) {
                    ButtonText("3", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("+")
                }, modifier = equalWeight) {
                    ButtonText("+", Orange)
                }
            }
            ButtonsRow(equalWeightRow) {
                KeyboardButton(onClick =
                {
                    areAllVisible = !areAllVisible
                    println("areAllVisible: $areAllVisible")
                }, modifier = equalWeight
                ) {
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (areAllVisible) 180F else 0F,
                        label = "\"Show all\" button rotation animation"
                    )
                    Image(
                        painter = painterResource(id = R.drawable.show_all),
                        contentDescription = "Delete last symbol",
                        colorFilter = ColorFilter.tint(Orange),
                        modifier = Modifier
                            .height(40.dp)
                            .rotate(rotationAngle)
                    )
                }
                if (areAllVisible) {
                    KeyboardButton(onClick = {
                        onInputButtonClicked()
                        viewModel.addToExpression("e")
                    }, modifier = equalWeight) {
                        ButtonText("e", Color.White)
                    }
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression("0")
                }, modifier = equalWeight) {
                    ButtonText("0", Color.White)
                }
                KeyboardButton(onClick = {
                    onInputButtonClicked()
                    viewModel.addToExpression(",")
                }, modifier = equalWeight) {
                    ButtonText(",", Color.White)
                }
                KeyboardButton(
                    onClick = {
                        viewModel.calculate()
                        isExpressionActive = false
                    },
                    modifier = equalWeight,
                    backgroundColor = Orange
                ) {
                    ButtonText("=", Color.White)
                }
            }
        }
    }

}

@Composable
private fun PreviousExpression(expression: String) {
    Text(
        text = expression,
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        fontSize = 20.sp,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Normal,
        color = MediumGray,
        textAlign = TextAlign.End
    )
}

@Composable
private fun ButtonsRow(modifier: Modifier, content: @Composable (() -> Unit)) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun KeyboardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkGray,
    content: @Composable (() -> Unit)
) {
    Button(
        modifier = modifier
            .padding(5.dp)
            .aspectRatio(1F),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonColors(
            containerColor = backgroundColor,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = backgroundColor
        ),
    ) {
        content()
    }
}

@Composable
private fun ButtonText(text: String, color: Color, superscriptText: String = "") {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = buildAnnotatedString {
                append(text)
                withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 15.sp)) {
                    append(superscriptText)
                }
            },
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth(),
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Normal,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MathLine(value: String, isActive: Boolean) {
    val iteractionSource = remember { MutableInteractionSource() }
    val textStyle = if (isActive) {
        TextStyle(
            fontSize = 30.sp,
            textAlign = TextAlign.End,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Serif,
            color = Color.White
        )
    } else {
        TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.End,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Serif,
            color = MediumGray
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        value = value,
        onValueChange = {},
        readOnly = false,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .onFocusChanged {
                keyboardController?.hide()
            },
        textStyle = textStyle,
        interactionSource = iteractionSource,
        keyboardOptions = KeyboardOptions.Default.copy(),

        )
}
