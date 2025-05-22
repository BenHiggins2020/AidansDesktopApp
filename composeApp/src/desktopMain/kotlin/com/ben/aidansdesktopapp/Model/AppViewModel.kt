package com.ben.aidansdesktopapp.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.aidansdesktopapp.Repository.HistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val mSymbolFlow = MutableStateFlow<String>("")
    private val mProgressFlow = MutableStateFlow(0)

    private val progressFlow = mProgressFlow.asStateFlow()
    private val symbolFlow = mSymbolFlow.asStateFlow()

    lateinit var map: Map<String, Double>
    val symbolsList = MutableStateFlow<List<String>>(emptyList())

    private val historicalData = mutableMapOf<String, HistoricalData>()
    private val dataHandlerModel = DataHandlerModel()


    fun collectSnP500Flow() = viewModelScope.launch(Dispatchers.IO) {
        dataHandlerModel.SnP500ExtracterFlow().collect {
            symbolsList.value = it
        }
        println("Collected symbols: ${symbolsList.value.size}")
    }

    fun collectHistoricalDataFlow() = viewModelScope.launch(Dispatchers.IO) {

        symbolsList.value.forEach {
            dataHandlerModel.historicalDataExtractorFlow(it).collect { data ->
                historicalData[data.symbol] = data
                mProgressFlow.value = (historicalData.size / symbolsList.value.size) * 100
                mSymbolFlow.value = data.symbol
            }
        }

    }

    fun collectHistoricalDataFlow(symbol: String) = viewModelScope.launch {
    }

    fun getSymbolFlow() = symbolFlow
    fun getProgressFlow() = progressFlow
    fun getSymbolListFlow() = symbolsList.asStateFlow()
}


