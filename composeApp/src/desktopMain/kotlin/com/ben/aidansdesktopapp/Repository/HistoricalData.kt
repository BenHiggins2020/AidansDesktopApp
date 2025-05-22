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
data class HistoricalData(
    val symbol: String,
    val rows: List<HistoricalDataRow>,
)
