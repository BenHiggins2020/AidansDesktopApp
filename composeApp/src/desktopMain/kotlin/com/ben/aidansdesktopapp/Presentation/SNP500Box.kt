package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale


@Composable
fun SNP500Box(modifier: Modifier = Modifier, dataSource: AppViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope() // Ensures correct coroutine execution

    Card(
        modifier = modifier
            .background(Color.Transparent, shape = CircleShape)
            .safeContentPadding()
    ) {
        Row(
            modifier = Modifier.background(Color.Transparent, shape = CircleShape),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(8.dp).weight(1f).background(Color.Yellow),
                text = "S&P 500 Stocks:",
                textDecoration = TextDecoration.Underline,
            )

            var text by remember { mutableStateOf("") }

            TextField(
                modifier = Modifier.background(Color.White).weight(1f),
                value = text,
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (dataSource.getSymbolListFlow().value.contains(text.uppercase())) {

                            coroutineScope.launch {
                                val index =
                                    dataSource.getSymbolListFlow().value.indexOf(text.uppercase())
                                listState.animateScrollToItem(index)
                            }

                        }
                    }
                ),
                readOnly = false,
                singleLine = true,
                maxLines = 1,
            )
        }

        ScrollableList(
            modifier = Modifier.background(Color.Transparent, shape = CircleShape),
            dataSource = dataSource,
            listState = listState
        )
    }

}