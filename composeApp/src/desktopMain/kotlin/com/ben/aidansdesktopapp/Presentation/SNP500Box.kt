package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ben.aidansdesktopapp.Model.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun SNP500Box(modifier: Modifier = Modifier, dataSource: AppViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope() // Ensures correct coroutine execution

    //Entire Container
    Card(
        modifier = Modifier
            .padding(16.dp)
            .safeContentPadding()
    ) {
        //Title Row
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically, // Ensures both components align properly
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(3f) // Ensures title takes most of the space
                    .padding(8.dp),
                text = "S&P 500 Stocks:",
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )

            var text by remember { mutableStateOf("") }

            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black, // Adjust cursor color as needed
                    focusedTextColor = Color.Black, // Ensures text is readable
                    unfocusedTextColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 12.sp), // Reduce text size

                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .weight(1f) // Takes up remaining space
                    .padding(8.dp), // Limits width instead of full size

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

                        } else {
                            PopUp.popUpText.value = "Symbol not found."
                            PopUp.popUpTrigger.targetState = true
                        }
                    }
                ),
                placeholder = {
                    BasicText(
                        text = "Search",
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 20.sp,
                            stepSize = 1.sp
                        ),
                        maxLines = 1,
                        style = TextStyle(fontSize = 16.sp)
                    )
                },
                readOnly = false,
                singleLine = true,
                maxLines = 1,
            )
        }

        //List Row
        Row(
            modifier = Modifier.fillMaxWidth().weight(4f).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically, // Ensures both components align properly
            horizontalArrangement = Arrangement.Center
        ) {
            ScrollableList(
                dataSource = dataSource,
                listState = listState
            )
        }


    }

}