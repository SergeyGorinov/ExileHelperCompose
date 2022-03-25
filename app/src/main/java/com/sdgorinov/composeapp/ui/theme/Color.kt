package com.sdgorinov.composeapp.ui.theme

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.TextFieldColorsWithIcons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Default = Color(0xFF1D262F)
val DefaultLight = Color(0xFF454E58)
val DefaultDark = Color(0xFF000005)
val Secondary = Color(0xFF546E7A)
val SecondaryLight = Color(0xFF819CA9)
val SecondaryDark = Color(0xFF29434E)
val Text = Color(0xFFFFF8E1)
val DisabledButton = Color(0x801D262F)
val DisabledButtonText = Color(0x80FFF8E1)

@ExperimentalMaterialApi
@Composable
fun exposedDropdownMenuColors() = object : TextFieldColorsWithIcons {

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(Default)
    }

    @Composable
    override fun cursorColor(isError: Boolean): State<Color> {
        return rememberUpdatedState(Text)
    }

    @Composable
    override fun indicatorColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        return rememberUpdatedState(Text)
    }

    @Composable
    override fun labelColor(
        enabled: Boolean,
        error: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        return rememberUpdatedState(SecondaryLight)
    }

    @Composable
    override fun leadingIconColor(enabled: Boolean, isError: Boolean): State<Color> {
        return rememberUpdatedState(Text)
    }

    @Composable
    override fun placeholderColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(SecondaryLight)
    }

    @Composable
    override fun textColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(Text)
    }

    @Composable
    override fun trailingIconColor(enabled: Boolean, isError: Boolean): State<Color> {
        return rememberUpdatedState(Text)
    }
}