package com.ben.aidansdesktopapp.Presentation.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShadowlessButton(onClick: () -> Unit, text: String){
    androidx.compose.material.Button(
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Transparent,
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        onClick = onClick,
    ) {
        Text(text)
    }
}