package com.ben.aidansdesktopapp.Adapter

import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.SharpeCalculator
import com.ben.aidansdesktopapp.Repository.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MarketDataHandler {
    /*
* Goal: create suspend functions to do the following
*   1. pull all the snp500 tickers
*   2. pull the historical data for each ticker
*   3. calculate the sharpe ratio for each ticker
*   4. output sharpe ratio for ticker in a file
*
* */

    /*Flow:
    *  1. call getSnP500 api
    *  2. call getHistoricalData api for each value in list (from step 1)
    *  3. call calculateSharpeRatio api for each Historical value obj in list (from step 2)
    *  4. output sharpe ratio, and historicalData to excel sheets. When Prompted!
    *
    *
    * */

    private val webService = WebService()
    private val sharpeCalculator = SharpeCalculator()

    private val mhistoricalDataFlow = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    val historicalDataFlow = mhistoricalDataFlow.asStateFlow()

    //WebService API Wrapper for getting all tickers.
     suspend fun getSnP500Symbols(): List<String> {
        return withContext(Dispatchers.IO) { webService.getSnP500Symbols() }
    }

    //WebService Api Wrapper for pulling historical data for a single ticker
     suspend fun getHistoricalData(symbol: String): HistoricalData {
        return withContext(Dispatchers.IO) { webService.createHistoricalData(symbol) }
    }

    private suspend fun calculateSharpeRatio(symbol: String): Map<String, Double> {
        return withContext(Dispatchers.Default) {
            val historicalData = getHistoricalData(symbol).also {
                mhistoricalDataFlow.value.toMutableMap().apply {
                    this[symbol] = it
                }
            }
            val sharpeRatio = sharpeCalculator.runCalculation(historicalData)
            val mutableMap = mutableMapOf<String, Double>()
            mutableMap[symbol] = sharpeRatio
            return@withContext mutableMap
        }
    }

    //Offline Api for doing sharpe calculation with HistoricalData
    private suspend fun calculateSharpeRatioForSingleStock(
        historicalData: HistoricalData
    ): Map<String, Double> {
        return withContext(Dispatchers.IO) {
            historicalData.also {
                mhistoricalDataFlow.value.toMutableMap().apply {
                    this[it.symbol] = it
                }
            }
            val sharpeRatio = sharpeCalculator.runCalculation(historicalData)

            val mutableMap = mutableMapOf<String, Double>()

            mutableMap[historicalData.symbol] = sharpeRatio

            return@withContext mutableMap
        }
    }

    //Exposed Api for calling the WebService APIs then passing data into calculator.
    suspend fun runWebServiceAndCalculateSharpeRatios(): Map<String, Double> {
        return withContext(Dispatchers.IO) {
            val symbols = getSnP500Symbols()
            val sharpeRatioMap = mutableMapOf<String, Double>()

            for (symbol in symbols) {
                val sharpeRatio = calculateSharpeRatio(symbol)
                sharpeRatioMap.putAll(sharpeRatio)
                println("Sharpe Ratio for $symbol: ${sharpeRatio[symbol]}")
            }

            return@withContext sharpeRatioMap
        }
    }

    //Exposed API for calculating sharpe ratio for a single stock history data.
    suspend fun getSharpeRatio(data: HistoricalData): Map<String, Double> {
        return withContext(Dispatchers.IO) {
            val sharpeRatioMap = mutableMapOf<String, Double>()
            val sharpeRatio = calculateSharpeRatioForSingleStock(data)
            sharpeRatioMap.putAll(sharpeRatio)

            return@withContext sharpeRatioMap
        }
    }



}