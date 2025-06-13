package com.ben.aidansdesktopapp

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.ben.aidansdesktopapp.Presentation.SNP500Box
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
//    System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe")

    val viewModel = AppViewModel()

    val symbolFlow = viewModel.getSymbolFlow()
    val progressFlow = viewModel.getProgressFlow()
    val popUpText = MutableStateFlow<String>("Practice")
    val popUpTrigger = MutableTransitionState<Boolean>(false)

    val popUp = PopUp.PopUpBuilder().withTransitionState(popUpTrigger).withText(popUpText).build()

    val enableButton by remember { mutableStateOf(symbolFlow.value.isNotEmpty()) }
    MaterialTheme {
        Scaffold {

            Column(Modifier.fillMaxSize()) {
                Row( // Master Row
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.75f)
                        .weight(1f)
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
                        val TAG = "App"
                        var txt by remember { mutableStateOf("") }
                        TextField(
                            value = txt,
                            onValueChange = { txt = it },
                            label = { Text("Enter ticker to get historical data for") },


                        )
                        Button(onClick = {
                            if(!txt.isNullOrEmpty()){
                                viewModel.makeSeleniumApiCall(txt)
                            } else {
                                PopUp.popUpText.value = "Please enter a ticker!"
                                PopUp.popUpTrigger.targetState = true
                            }
                        }){
                            Text("Search for ticker: $txt")
                        }
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(.25f).background(Color.White)
                        .weight(.20f)
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
                            if (viewModel.getSymbolListFlow().value.isNotEmpty()) {
                                viewModel.collectHistoricalDataFlow()
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
        }

        popUp.show()

    }

}
