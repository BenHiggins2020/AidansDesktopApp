package com.ben.aidansdesktopapp.Presentation.pages.data

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import com.ben.aidansdesktopapp.Presentation.CustomScrollableList
import com.ben.aidansdesktopapp.Presentation.CustomStaticScrollableList
import com.ben.aidansdesktopapp.Presentation.ScrollableList
import com.ben.aidansdesktopapp.Presentation.components.Screen
import com.ben.aidansdesktopapp.Presentation.components.ScrollbarList
import com.ben.aidansdesktopapp.Presentation.components.Table
import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.HistoricalDataRow
import com.ben.aidansdesktopapp.Repository.emptyHistoricalData


@Composable
fun Data(viewModel: AppViewModel) {
    val historicalDataFlow = viewModel.getHistoricalDataFlow() //List of historical data.
    var symbolSelectionTrigger = MutableTransitionState<Boolean>(initialState = false)
    val historicalDataList = historicalDataFlow.collectAsState()
    var selectedSymbolLocal by remember { mutableStateOf<String>("Select a Symbol") }
    var selectedSymbolData by remember { mutableStateOf(emptyHistoricalData) }
    try {
        if(historicalDataList.value.isEmpty()){
            println("Historical Data List is empty. Doing nothing... ")
        } else {
            selectedSymbolData =
                historicalDataList.value.first()
        }

    } catch (e: Exception) {
        println("Probably got empty data... we wont do anything right now! ")
        selectedSymbolData = emptyHistoricalData
    }
    Screen {


        Box(
            modifier = Modifier.background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = selectedSymbolLocal,
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        println("Button Clicked  current selection trigger state = ${symbolSelectionTrigger.targetState}")
                        symbolSelectionTrigger.targetState = true
                    },
                    modifier = Modifier.padding(16.dp)

                ) {
                    Text("Select Symbol")
                }
            }

        }

        if (!symbolSelectionTrigger.targetState) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                //TODO: update this with a way to select different tickers.
                val dataListState = rememberLazyListState()
              /*  Row(
//                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp).fillMaxWidth().background(Color.LightGray),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        "Date ",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    *//*Text(
                        text = "|",
                        modifier = Modifier.padding(end = 16.dp)
                    )*//*
                    Text(
                        text = "Adj. Close",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }*/


                Table(data = selectedSymbolData)


              /*  CustomStaticScrollableList(
                    items = selectedSymbolData,
                    listState = dataListState,
                    listItem = { items ->
                        items as HistoricalDataRow
                        if (!items.date.isNullOrEmpty()) {
                            Row(
//                                modifier = Modifier.padding(16.dp).fillMaxSize()
//                                    .background(Color.Gray),
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                Text(
                                    text = items.date,
//                                    modifier = Modifier.padding(end = 16.dp)
                                )
                               *//* Text(
                                    text = "|",
//                                    modifier = Modifier.padding(end = 16.dp)
                                )*//*
                                Text(
                                    text = items.adjClose,
//                                    modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                        }

                    }
                )*/
            }
        }

        AnimatedVisibility(
            visibleState = symbolSelectionTrigger,
            enter = slideInVertically(),
            exit = slideOutVertically(),
            modifier = Modifier.fillMaxWidth().padding(24.dp)

        ) {

            val listState = rememberLazyListState()

            CustomScrollableList(
                dataSource = viewModel.getSymbolCollectionFlow(),
                listState = listState,
                listItem = {
                    //TODO: not using.
                    it as String
                    Text(text = it,
                        modifier = Modifier.padding(16.dp).clickable {
                            println("Clicked on $it")
                            symbolSelectionTrigger.targetState = false

                        }
                    )
                },
                onClick = { str: String ->
                    println("Clicked on $selectedSymbolLocal")
                    println("Also got a str: $str")
                    symbolSelectionTrigger.targetState = false

                    selectedSymbolLocal = str

                    println("selectedSymbolLocal: ${selectedSymbolLocal}")
                    selectedSymbolData = historicalDataFlow.value.filter { it.symbol == str }.first()

                }

            )
            /*ScrollbarList(
                listData = symbols.value,
                listItem = {
                    it as String
                    Text(text = it,
                        modifier = Modifier.padding(16.dp).clickable {
                            println("Clicked on $it")
                            selectedSymbol.value = it
                            symbolSelectionTrigger.targetState = false

                        }
                    )
                }
            )*/


        }

    }
}
