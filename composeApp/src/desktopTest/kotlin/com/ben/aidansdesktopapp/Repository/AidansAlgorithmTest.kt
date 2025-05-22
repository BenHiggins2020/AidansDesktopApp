package com.ben.aidansdesktopapp.Repository

import org.junit.Test

class AidansAlgorithmTest {

    val testInstance = SharpeCalculator()
    private val historicalData = getMockHistoricalData("AAPL")

    fun getMockHistoricalData(symbol: String): HistoricalData {
        val mockRows = listOf(
            HistoricalDataRow("2020-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550,000"),
            HistoricalDataRow("2021-05-20", "156.15", "159.20", "153.70", "157.90", "157.60", "1,680,000"),
            HistoricalDataRow("2022-05-20", "170.80", "174.50", "168.30", "172.40", "172.10", "1,720,000"),
            HistoricalDataRow("2023-05-20", "183.90", "187.50", "181.00", "185.20", "185.00", "1,800,000"),
            HistoricalDataRow("2024-05-20", "196.30", "200.20", "194.00", "198.80", "198.50", "1,950,000")
        )

        return HistoricalData(symbol, mockRows)
    }


    @Test
    fun `test sharpeRatio`() {
        val sharpeRation = testInstance.runCalculation(historicalData)
        assert(sharpeRation > 0)
    }


}