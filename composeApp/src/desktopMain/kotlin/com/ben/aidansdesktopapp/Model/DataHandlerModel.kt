package com.ben.aidansdesktopapp.Model

import com.ben.aidansdesktopapp.Adapter.MarketDataHandler
import com.ben.aidansdesktopapp.Repository.HistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataHandlerModel {

    val marketDataHandler = MarketDataHandler()
    val historicalDataMap = mutableMapOf<String, HistoricalData>()

    fun SnP500ExtracterFlow() = flow<List<String>> {
        emit(marketDataHandler.getSnP500Symbols())
    }.flowOn(Dispatchers.IO)

    fun historicalDataExtractorFlow(symbol: String) = flow<HistoricalData> {
        emit(marketDataHandler.getHistoricalData(symbol))
    }.flowOn(Dispatchers.IO)

    fun beginCalculationFlow(data:HistoricalData) = flow<Map<String, Double>> {
        emit(marketDataHandler.getSharpeRatio(data))
    }.flowOn(Dispatchers.IO)

}