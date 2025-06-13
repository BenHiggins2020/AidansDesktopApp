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
        println("SnP500ExtracterFlow called")
        emit(marketDataHandler.getSnP500Symbols())
    }.flowOn(Dispatchers.IO)

    fun historicalDataExtractorFlow(symbol: String) = flow<HistoricalData> {
        println("Historical data extractor flow called")

        if (historicalDataMap.containsKey(symbol)) {
            emit(historicalDataMap[symbol]!!)
            println("historical data stored, fetching... ${historicalDataMap[symbol]!!}")

        } else {
            println("No historical data stored, fetching...")
            emit(marketDataHandler.getHistoricalData(symbol))
        }

    }.flowOn(Dispatchers.IO)

    fun beginCalculationFlow(data: HistoricalData) = flow<Map<String, Double>> {
        emit(marketDataHandler.getSharpeRatio(data))
    }.flowOn(Dispatchers.IO)

}