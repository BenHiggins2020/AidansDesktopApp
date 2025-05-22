package com.ben.aidansdesktopapp

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import com.ben.aidansdesktopapp.Presentation.PopUp
import com.ben.aidansdesktopapp.Presentation.SNP500Box
import com.ben.aidansdesktopapp.Presentation.ScrollableList
import io.reactivex.rxjava3.kotlin.toObservable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val viewModel = AppViewModel()

    val symbolFlow = viewModel.getSymbolFlow()
    val progressFlow = viewModel.getProgressFlow()
    val popUpText = MutableStateFlow<String>("Practice")
    val popUpTrigger = MutableTransitionState<Boolean>(false)

    val popUp = PopUp.PopUpBuilder()
        .withTransitionState(popUpTrigger)
        .withText(popUpText)
        .build()

    MaterialTheme {
        Scaffold {

                Row( // Master Row
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.25f)
                            .fillMaxHeight()
                            .background(Color.Green),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {

                        SNP500Box(
                            modifier = Modifier
                                .padding(16.dp),
                            dataSource = viewModel
                        )

                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .safeContentPadding()
                            .background(Color.Yellow),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Hello Aidan!")

                        Button(
                            onClick = {
                                viewModel.collectSnP500Flow()
                            }
                        ) {
                            Text("Get SnP 500 tickers!")
                        }

                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .safeContentPadding()
                            .background(Color.Red),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Right Column")

                    }

                }
                popUp.show()



        }

    }
}