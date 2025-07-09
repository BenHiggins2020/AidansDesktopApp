package com.ben.aidansdesktopapp.Presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.onClick
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
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun ScrollableList(
    dataSource: AppViewModel,
    listState: LazyListState
) {

    val items by dataSource.getSymbolListFlow().collectAsState()


    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Scrollbar properly linked to LazyColumn
        VerticalScrollbar(
            modifier = Modifier.padding(start = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )

        LazyColumn(
            modifier = Modifier,
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
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
            .background(Color.Transparent, shape = CircleShape),

        ) {
        Text(
            text = text,
            modifier = Modifier
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

@Composable
fun CustomScrollableList(
    dataSource: StateFlow<List<Any>>,
    listState: LazyListState,
    listItem: @Composable (Any) -> Unit,
    onClick: (String) -> Unit
) {

    val items by dataSource.collectAsState()


    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Scrollbar properly linked to LazyColumn
        VerticalScrollbar(
            modifier = Modifier.padding(start = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )

        LazyColumn(
            modifier = Modifier,
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                CustomListItem(
                    text = items[index].toString(),
                    onClick = onClick

                )
            }
        }
    }
}

@Composable
fun CustomListItem(
    text: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .border(width = 2.dp, color = Color.Black)
            .background(Color.Transparent, shape = CircleShape)
            .fillMaxSize(.33f).clickable {
                onClick(text)
            }


    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            maxLines = 3
        )
    }

}

@Composable
fun CustomStaticScrollableList(
    items: List<Any>,
    listState: LazyListState,
    listItem : @Composable (Any) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Scrollbar properly linked to LazyColumn
        VerticalScrollbar(
            modifier = Modifier.padding(start = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )

        LazyColumn(
            modifier = Modifier,
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                listItem(
                    items[index],
                )
            }
        }
    }
}



