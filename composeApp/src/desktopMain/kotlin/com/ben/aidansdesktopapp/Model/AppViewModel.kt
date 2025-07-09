package com.ben.aidansdesktopapp.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.aidansdesktopapp.Adapter.ApiCallManager
import com.ben.aidansdesktopapp.Repository.HistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val mSymbolFlow = MutableStateFlow<String>("")
    private val mProgressFlow = MutableStateFlow(0f)

    private val progressFlow = mProgressFlow.asStateFlow()
    private val symbolFlow = mSymbolFlow.asStateFlow()

    lateinit var map: Map<String, Double>

    val snp500Symbols = MutableStateFlow<List<String>>(emptyList())

    private val mHistoricalDataFlow = MutableStateFlow<MutableList<HistoricalData>>(mutableListOf())
    private val mSymbolCollection = MutableStateFlow<MutableList<String>>(emptyList<String>().toMutableList())
    private val dataHandlerModel = DataHandlerModel()


    fun makeSeleniumApiCall(symbol: String) = viewModelScope.launch {
        println("Making selenium api call. for symbol: $symbol")
        val data = ApiCallManager().makeLocalSeleniumApiCall(symbol)

        println("Selenium api call complete. Updating Data structures... with ${data.rows.size} ")
        println("Checking for current data: ${data.rows.size}")

        mHistoricalDataFlow.value.add(data)
        mSymbolCollection.value.add(data.symbol)

        println("Data structures updated. Checking: (data) ${mHistoricalDataFlow.value.size}")
        println("Symbol Collection updated. Checking: (data) ${mSymbolCollection.value.size}")


    }

    fun collectSnP500Flow() = viewModelScope.launch(Dispatchers.IO) {
        dataHandlerModel.SnP500ExtracterFlow().collect {
            snp500Symbols.value = it
        }
        println("Collected symbols: ${snp500Symbols.value.size}")
    }

    fun collectHistoricalDataForSymbol() = viewModelScope.launch {
        println("Collecting historical data for ${snp500Symbols.value.size} symbols")
        snp500Symbols.value.forEach {
            println("Current Symbol: $it")
            mSymbolFlow.value = it
            mSymbolFlow.emit(it)
            collectHistoricalDataForSymbol(it)
        }

    }

    private fun collectHistoricalDataForSymbol(symbol: String) = viewModelScope.launch {
        println("Collecting historical data for $symbol")
        dataHandlerModel.historicalDataExtractorFlow(symbol).collect { data ->
            val list = mHistoricalDataFlow.value
            println("Checking for current data: ${data.rows.size}")

            list.toMutableList().add(data)
            mHistoricalDataFlow.value = list
            println("Data structures updated. Checking: (list) ${list.size} (data) ${mHistoricalDataFlow.value.size}")
            mProgressFlow.emit((list.size / snp500Symbols.value.size).toFloat())
        }
    }

    fun getHistoricalData(symbol: String) = viewModelScope.launch {
        println("getHistorical Data for symbol: $symbol")
        mSymbolFlow.value = symbol
        dataHandlerModel.historicalDataExtractorFlow(symbol)
    }

    /**
     * This is used when doing the large parse. it is used for tracking which ticker we are currently parsing.
     * */
    fun getCurrentSymbolSearchFlow() = symbolFlow
    /**
     * This is used during the full parse, Which would indicate the percentage of completion of the parsing.
     * */
    fun getProgressFlow() = progressFlow

    /**
     * This is used for getting all of the Snp500 symbols (from wikipedia)
     */
    fun getSnP500SymbolsFlow() = snp500Symbols.asStateFlow()

    /**
     * This is the flow used to keep an updated list of the historical data, that has been parsed.
     */
    fun getHistoricalDataFlow() = mHistoricalDataFlow.asStateFlow()

    /**
     * This will keep a list of all of the symbols that have been searched for and parsed.
     */
    fun getSearchedSymbolListFlow() = mSymbolCollection.asStateFlow()
}


