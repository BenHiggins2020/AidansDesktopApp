package com.ben.aidansdesktopapp.Repository

import com.ben.aidansdesktopapp.Repository.web.JsoupWebService
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class JsoupWebServiceTest {

    val testInstance = JsoupWebService()

    private lateinit var wikiList:List<String>
    private lateinit var mostActiveTableMap:Map<String, TableData>

    @Before
    fun setUp() {
//        mostActiveTableMap = testInstance.getMostActive()
//        wikiList = testInstance.getSnP500Symbols()
    }

    @Test
    fun `test mostActive table is not empty`(){
        assert(mostActiveTableMap.isNotEmpty())
    }

    @Test
    fun `test wiki snp 500 list is not empty`(){
        assertTrue(wikiList.isNotEmpty())
    }

    @Test
    fun `test createUrl for historical data is correct`(){
        val symbol = "AAPL"
        val url = testInstance.createHistoricalDataUrlFor5YPeriod(symbol)
        println("Got url: $url")
        assertTrue(url.contains(symbol))
    }

    @Test
    fun `test parseHistoricalData returns data`(){
        val symbol = "AAPL"
        val data = testInstance.parseHistoricalDataAsync(symbol)
        assertTrue(data.isNotEmpty())
    }
}