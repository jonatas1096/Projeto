package com.connectstudent.projeto.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.connectstudent.projeto.R

// Set of Material typography styles to start with

val Jomhuria = FontFamily(
    Font(R.font.jomhuria_regular),
    Font(R.font.jomhuria_regular, FontWeight.Bold)
)

val JomhuriaRegular = FontFamily(
    Font(R.font.jomhuria_regular),
    Font(R.font.jomhuria_regular)
)

val Dongle = FontFamily(
    Font(R.font.dongle_regular),
    Font(R.font.dongle_bold, FontWeight.Bold),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )






    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)