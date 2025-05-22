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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val viewModel = AppViewModel()

    val symbolFlow = viewModel.getSymbolFlow()
    val progressFlow = viewModel.getProgressFlow()
    val popUpText = MutableStateFlow<String>("Practice")
    val popUpTrigger = MutableTransitionState<Boolean>(false)

    val popUp = PopUp.PopUpBuilder().withTransitionState(popUpTrigger).withText(popUpText).build()

    MaterialTheme {
        Scaffold {

            Column(Modifier.fillMaxSize()){
                Row( // Master Row
                    modifier = Modifier.safeContentPadding().fillMaxWidth().fillMaxHeight(.75f).weight(1f)
                        .background(Color.DarkGray),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1.25f).fillMaxHeight().background(Color.Green),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {

                        SNP500Box(
                            modifier = Modifier.padding(16.dp), dataSource = viewModel
                        )

                    }

                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight().safeContentPadding()
                            .background(Color.Yellow),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Hello Aidan!")


                    }
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight().safeContentPadding()
                            .background(Color.Red),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Right Column")

                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(.25f).background(Color.White).weight(.20f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        viewModel.collectSnP500Flow()
                    }) {
                        Text("Get SnP 500 tickers!")
                    }
                }
            }

            popUp.show()

        }

    }
}