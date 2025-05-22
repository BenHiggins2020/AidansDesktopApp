package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel


@Composable
fun SNP500Box(modifier: Modifier = Modifier, dataSource: AppViewModel) {

    Card(
        modifier = modifier
            .background(Color.Red, shape = CircleShape)
            .safeContentPadding()
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "S&P 500 Stocks:",
            textDecoration = TextDecoration.Underline,
        )
        ScrollableList(
            modifier = Modifier,
            dataSource = dataSource,
        )
    }

}