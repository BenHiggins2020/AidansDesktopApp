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


val emptyHistoricalData = HistoricalData(
    symbol = "",
    rows = emptyList()
)


data class HistoricalDataTable(
    val dates: MutableList<String>,
    val open: MutableList<String>,
    val high: MutableList<String>,
    val low: MutableList<String>,
    val close: MutableList<String>,
    val adjClose: MutableList<String>,
    val volume: MutableList<String>
)

fun HistoricalData.toTable(): HistoricalDataTable {
    val table = HistoricalDataTable(
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList(),
        emptyList<String>().toMutableList()
    )
    this.rows.forEach {
        table.dates.add(it.date)
        table.open.add(it.open)
        table.high.add(it.high)
        table.low.add(it.low)
        table.close.add(it.close)
        table.adjClose.add(it.adjClose)
        table.volume.add(it.volume)
    }
    return table

}