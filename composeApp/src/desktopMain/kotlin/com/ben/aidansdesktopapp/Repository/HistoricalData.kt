package com.ben.aidansdesktopapp.Repository

data class HistoricalDataRow(
    val date: String,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val adjClose: String,
    val volume: String,
)

data class mutableHistoricalDataRow(
    var date: String = "",
    var open: String = "",
    var high: String = "",
    var low: String = "",
    var close: String = "",
    var adjClose: String = "",
    var volume: String = "",
)

//extension function
fun mutableHistoricalDataRow.toHistoricalDataRow(): HistoricalDataRow {
    return HistoricalDataRow(
        date = this.date,
        open = this.open,
        high = this.high,
        low = this.low,
        close = this.close,
        adjClose = this.adjClose,
        volume = this.volume
    )
}

fun List<HistoricalDataRow>.toHistoricalData(symbol: String): HistoricalData {
    return HistoricalData(
        symbol = symbol,
        rows = this
    )
}


data class HistoricalData(
    val symbol: String,
    val rows: List<HistoricalDataRow>,
)
