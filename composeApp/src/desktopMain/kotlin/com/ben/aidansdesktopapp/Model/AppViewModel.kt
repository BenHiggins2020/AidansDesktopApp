package com.ben.aidansdesktopapp.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.aidansdesktopapp.Adapter.ApiCallManager
import com.ben.aidansdesktopapp.Repository.HistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val mSymbolFlow = MutableStateFlow<String>("")
    private val mProgressFlow = MutableStateFlow(0f)

    private val progressFlow = mProgressFlow.asStateFlow()
    private val symbolFlow = mSymbolFlow.asStateFlow()

    lateinit var map: Map<String, Double>
    val symbolsList = MutableStateFlow<List<String>>(emptyList())

    private val historicalData = mutableMapOf<String, HistoricalData>()
    private val mHistoricalDataFlow = MutableStateFlow<Map<String, HistoricalData>>(historicalData)
    private val historicalDataFlow = mHistoricalDataFlow.asStateFlow()

    private val dataHandlerModel = DataHandlerModel()


    fun makeSeleniumApiCall(symbol: String) = viewModelScope.launch {
        println("Making selenium api call. for symbol: $symbol")
        ApiCallManager().makeLocalSeleniumApiCall(symbol)
    }

    fun collectSnP500Flow() = viewModelScope.launch(Dispatchers.IO) {
        dataHandlerModel.SnP500ExtracterFlow().collect {
            symbolsList.value = it
        }
        println("Collected symbols: ${symbolsList.value.size}")
    }

    fun collectHistoricalDataFlow() = viewModelScope.launch {
        println("Collecting historical data for ${symbolsList.value.size} symbols")
        symbolsList.value.forEach {
            println("Current Symbol: $it")
            mSymbolFlow.value = it
            mSymbolFlow.emit(it)
            collectHistoricalDataFlow(it)
           /* dataHandlerModel.historicalDataExtractorFlow(it).collect { data ->
                historicalData[data.symbol] = data
                mProgressFlow.emit((historicalData.size / symbolsList.value.size).toFloat())
            }*/

        }

    }

    fun collectHistoricalDataFlow(symbol: String) = viewModelScope.launch {
        println("Collecting historical data for $symbol")
        dataHandlerModel.historicalDataExtractorFlow(symbol).collect { data ->
            historicalData[data.symbol] = data
            mHistoricalDataFlow.value = historicalData
            mProgressFlow.emit((historicalData.size / symbolsList.value.size).toFloat())
        }
    }

    fun getHistoricalData(symbol: String) = viewModelScope.launch {
        println("getHistorical Data for symbol: $symbol")
        mSymbolFlow.value = symbol
        dataHandlerModel.historicalDataExtractorFlow(symbol)
    }

    fun getSymbolFlow() = symbolFlow
    fun getProgressFlow() = progressFlow
    fun getSymbolListFlow() = symbolsList.asStateFlow()
    fun getHistoricalDataFlow() = historicalDataFlow
}


