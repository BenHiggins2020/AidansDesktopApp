package com.ben.aidansdesktopapp.Presentation.pages.main

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import com.ben.aidansdesktopapp.Presentation.PopUp
import com.ben.aidansdesktopapp.Presentation.components.SNP500Box
import com.ben.aidansdesktopapp.Presentation.components.Screen
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun Home(
    viewModel: AppViewModel = AppViewModel()
) {
  Screen { content(viewModel) }
}

@Composable
fun content(viewModel: AppViewModel) {
    val symbolFlow = viewModel.getCurrentSymbolSearchFlow()
    val progressFlow = viewModel.getProgressFlow()
    val popUpText = MutableStateFlow<String>("Practice")
    val popUpTrigger = MutableTransitionState<Boolean>(false)

    Row( // Master Row
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.75f)
            .safeContentPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.weight(1.25f).fillMaxHeight()
                .padding(4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            SNP500Box(
                dataSource = viewModel
            )

        }

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight().safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Hello Aidan!")

            LinearProgressIndicator(
                progress = progressFlow.value.toFloat(),
                modifier = Modifier.padding(16.dp),

                )

            Text("Progress: ${progressFlow.value * 100}%")
            Text("Symbol: ${symbolFlow.value}")


        }

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight().safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Right Column")
            var ticker by remember { mutableStateOf("") }
            TextField(
                value = ticker,
                onValueChange = { ticker = it.uppercase() },
                label = { Text("Enter ticker to get historical data for") },


                )
            Button(onClick = {
                if (!ticker.isNullOrEmpty() && !viewModel.getSnP500SymbolsFlow().value.contains(ticker)) {
                    viewModel.makeSeleniumApiCall(ticker)
                } else {
                    PopUp.popUpText.value = "Please enter a ticker! That you haven't already gotten historical data for!"
                    PopUp.popUpTrigger.targetState = true
                }
            }) {
                Text("Search for ticker: $ticker")
            }
        }

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            viewModel.collectSnP500Flow()
        }) {
            Text("Get SnP 500 tickers!")
        }

        Button(
            enabled = true,
            onClick = {
                if (viewModel.getSnP500SymbolsFlow().value.isNotEmpty()) {
                    viewModel.collectHistoricalDataForSymbol()
                } else {
                    PopUp.popUpText.value = "Please Get SnP 500 tickers first!"
                    PopUp.popUpTrigger.targetState = true
                }
            }
        ) {
            Text("Collect Historical Data")
        }


    }
}