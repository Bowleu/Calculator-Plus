package com.bowleu.probcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .background(color = Color.DarkGray)
                    .fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)) {

                }
                Column(modifier = Modifier.weight(0.6f)){
                    ButtonsRow(Modifier.weight(1f),
                        @Composable { ButtonText("AC", Color.White) } to {println("Нажата кнопка AC")},
                        @Composable { ButtonText("BACK", Color.White) } to {println("Нажата кнопка BACK")},
                        @Composable { ButtonText("%", Color.White) } to {println("Нажата кнопка %")},
                        @Composable { ButtonText("/", Color.White) } to {println("Нажата кнопка /")},
                    )
                    ButtonsRow(Modifier.weight(1f),
                        @Composable { ButtonText("7", Color.White) } to {println("Нажата кнопка 7")},
                        @Composable { ButtonText("8", Color.White) } to {println("Нажата кнопка 8")},
                        @Composable { ButtonText("9", Color.White) } to {println("Нажата кнопка 9")},
                        @Composable { ButtonText("x", Color.White) } to {println("Нажата кнопка x")},
                    )
                    ButtonsRow(Modifier.weight(1f),
                        @Composable { ButtonText("4", Color.White) } to {println("Нажата кнопка 4")},
                        @Composable { ButtonText("5", Color.White) } to {println("Нажата кнопка 5")},
                        @Composable { ButtonText("6", Color.White) } to {println("Нажата кнопка 6")},
                        @Composable { ButtonText("-", Color.White) } to {println("Нажата кнопка -")},
                    )
                    ButtonsRow( Modifier.weight(1f),
                        @Composable { ButtonText("1", Color.White) } to {println("Нажата кнопка 1")},
                        @Composable { ButtonText("2", Color.White) } to {println("Нажата кнопка 2")},
                        @Composable { ButtonText("3", Color.White) } to {println("Нажата кнопка 3")},
                        @Composable { ButtonText("+", Color.White) } to {println("Нажата кнопка +")},
                    )
                    ButtonsRow( Modifier.weight(1f),
                        @Composable { ButtonText("->", Color.White) } to {println("Нажата кнопка ->")},
                        @Composable { ButtonText("0", Color.White) } to {println("Нажата кнопка 0")},
                        @Composable { ButtonText(",", Color.White) } to {println("Нажата кнопка ,")},
                        @Composable { ButtonText("=", Color.White) } to {println("Нажата кнопка =")},
                    )
                }
            }
        }
    }

    @Composable
    private fun ButtonsRow(modifier: Modifier, vararg buttons: Pair<@Composable () -> Unit, () -> Unit>) {
        Row(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 1.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            for (button in buttons) {
                KeyboardButton(button.first, button.second, Modifier.weight(1f))
            }
        }
    }

    @Composable
    private fun KeyboardButton(content: @Composable () -> Unit, onClick: () -> Unit, modifier: Modifier = Modifier, backgroundColor: Color = Color.LightGray) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                containerColor = backgroundColor,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = backgroundColor
            ),
            modifier = modifier
                .padding(10.dp)
                .aspectRatio(1F)
        ) {
            content()
        }
    }

    @Composable
    private fun ButtonText(text: String, color: Color) {
        Text(text = text,
            modifier = Modifier.background(Color.Transparent)
                .fillMaxSize()
                .wrapContentHeight(Alignment.CenterVertically),
            fontSize = 13.sp,
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Normal,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }


}



