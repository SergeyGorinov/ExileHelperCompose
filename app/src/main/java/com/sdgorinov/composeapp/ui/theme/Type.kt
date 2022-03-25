package com.sdgorinov.composeapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sgorinov.exilehelper.core.R

val DefaultFont = Typography(
    body1 = TextStyle(
        color = Text,
        fontFamily = FontFamily(Font(R.font.fontinsmallcaps)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)