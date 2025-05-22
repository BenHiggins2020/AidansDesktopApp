package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun ScrollableList(
    dataSource: AppViewModel,
    modifier: Modifier = Modifier,
    listState:LazyListState
) {

//    val listState = rememberLazyListState() // Correct state for LazyColumn
    val items by dataSource.getSymbolListFlow().collectAsState()


    Row(modifier = Modifier.fillMaxSize()) {
        // Scrollbar properly linked to LazyColumn
        VerticalScrollbar(
            modifier = Modifier.padding(start = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )

        LazyColumn(
            modifier = Modifier
                .background(Color.Transparent),//.weight(1f),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
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

            .background(Color.Transparent, shape = CircleShape),

        ) {
        Text(
            text = text,
            modifier = Modifier
                .background(Color.Yellow)
                .clickable {
                    println("Clicked on $text")
                }
                .fillMaxSize()

                .padding(16.dp)
                .wrapContentSize(),
            maxLines = 3
        )
    }
}
