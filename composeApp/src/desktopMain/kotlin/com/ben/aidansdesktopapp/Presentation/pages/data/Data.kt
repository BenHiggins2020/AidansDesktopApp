package com.ben.aidansdesktopapp.Presentation.pages.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import com.ben.aidansdesktopapp.Presentation.components.Screen
import com.ben.aidansdesktopapp.Presentation.components.ShadowlessButton

@Composable
fun Data(
    viewModel: AppViewModel
) {
    var screenSelection by remember { mutableStateOf("default") }

    topMenu(
        onClick = {
            println("onClick: $it")
            screenSelection = it
        }
    )
    Screen {


        when (screenSelection) {
            "historicalData" -> SymbolTableData(viewModel)
            "sharpe" -> SharpeComparison()
            "default" -> Default()

        }
    }
}

@Composable
fun topMenu(onClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ShadowlessButton(
            onClick = {
                println("Calling on click? ")
                onClick("historicalData")
            },
            text = "Historical Data"
        )

        Button(
            onClick = { onClick("sharpe") }
        ) {
            Text("Sharpe Data Comparison")
        }
    }
}

@Composable
fun Default() {
    Text("Select an option for comparing data.")
}