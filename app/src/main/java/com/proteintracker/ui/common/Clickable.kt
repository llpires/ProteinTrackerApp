package com.proteintracker.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.detectTapGestures

@Composable
fun Modifier.clickable(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = modifier.pointerInput(Unit) {
    detectTapGestures(onTap = onClick)
}