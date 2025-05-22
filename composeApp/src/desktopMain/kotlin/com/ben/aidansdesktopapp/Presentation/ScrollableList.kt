package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScrollableList(dataSource: AppViewModel, items: List<String> = emptyList(), modifier: Modifier = Modifier) {

    val listState = rememberLazyListState() // Correct state for LazyColumn
    val items by dataSource.getSymbolListFlow().collectAsState()

    Row(modifier = Modifier.fillMaxSize()) {

        // Scrollbar properly linked to LazyColumn
        VerticalScrollbar(
            modifier = Modifier.padding(start = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState // Use LazyListState instead of ScrollState
        ) {
            items(items.size) { index ->
                ListItem(text = items[index])
            }
        }
    }
}

@Preview
@Composable
fun ListItem(text: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .background(Color.White)
            .clickable {
                println("Clicked on $text")
            }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(),
            maxLines = 3
        )
    }
}
