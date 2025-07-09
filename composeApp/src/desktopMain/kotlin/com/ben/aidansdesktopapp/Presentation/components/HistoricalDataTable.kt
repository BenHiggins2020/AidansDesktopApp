package com.ben.aidansdesktopapp.Presentation.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.toTable

@Composable
fun Table(data: HistoricalData) {
    val tbl = data.toTable()
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {


        Column {

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.LightGray),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Date")
                Text("Adj Close")
            }

            Column(modifier = Modifier.verticalScroll(scrollState).background(Color.Gray)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(

                    ) {
                        tbl.dates.forEach { i ->
                            Text(i)
                        }
                    }
                    Column(

                    ) {

                        tbl.adjClose.forEach { i ->
                            Text(i)
                        }
                    }

                }

            }

        }
    }

}