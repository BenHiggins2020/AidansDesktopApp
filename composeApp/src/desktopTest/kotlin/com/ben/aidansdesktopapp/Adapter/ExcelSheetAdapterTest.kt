package com.ben.aidansdesktopapp.Adapter

import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.HistoricalDataRow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertTrue

class ExcelSheetAdapterTest {

    val historicalDataRow1 = HistoricalDataRow(
        "2023-05-20",
        "145.23",
        "147.90",
        "143.50",
        "146.80",
        "146.80",
        "1,550"
    )

    val historicalDataRow2 = HistoricalDataRow(
        "2023-05-21",
        "155.23",
        "155.90",
        "78.50",
        "146.80",
        "146.80",
        "1,550"
    )
    val historicalData = HistoricalData("BEN", listOf(historicalDataRow1, historicalDataRow2))

    @Test
    fun testOutputExcelSheet() {
        val workbook = ExcelSheetAdapter.Companion.ExcelSheetBuilder()
            .withSheet(
                sheetName = "StockTicker1",
                headers = listOf("Date", "Open", "High", "Low", "Close", "Adj Close", "Volume"),
                data = listOf(
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),

                    )
            )
            .withSheet(
                sheetName = "StockTicker2",
                headers = listOf("Date", "Open", "High", "Low", "Close", "Adj Close", "Volume"),
                data = listOf(
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),
                    listOf("2023-05-20", "145.23", "147.90", "143.50", "146.80", "146.80", "1,550"),
                )
            )
            .build()

        ExcelSheetAdapter.outputExcelSheet(workbook, "test.xlsx")
        assert(true)

    }

    @Test
    fun testOutputWithHistoricalDataRow() {

        val convertedData = HistoricalAndSharpeDataAdapter.convertHistoricalRowDataToExcelFormat(
            listOf(
                HistoricalDataRow(
                    "2023-05-20",
                    "145.23",
                    "147.90",
                    "143.50",
                    "146.80",
                    "146.80",
                    "1,550"
                ),
                HistoricalDataRow(
                    "2023-05-21",
                    "155.23",
                    "155.90",
                    "78.50",
                    "146.80",
                    "146.80",
                    "1,550"
                )
            )
        )

        assertTrue(convertedData.size == 2)
        assert(convertedData[0].isNotEmpty())

        println("Converted Data:")
        convertedData.forEach {
            println(it.toString())
        }

        val workbook = ExcelSheetAdapter.Companion.ExcelSheetBuilder()
            .withSheet(
                sheetName = "StockTicker1",
                headers = listOf("Date", "Open", "High", "Low", "Close", "Adj Close", "Volume"),
                data = convertedData
            ).build()

        ExcelSheetAdapter.outputExcelSheet(workbook, "test.xlsx")
        assert(true)

    }

    @Test
    fun testOutputCreatedWithConvertedHistoricalData() {
        val convertedData = HistoricalAndSharpeDataAdapter.convertHistoricalDataToExcelFormat(
            historicalData
        )
        val ws = ExcelSheetAdapter.Companion.ExcelSheetBuilder()
            .withSheet(
                sheetName = historicalData.symbol,
                headers = listOf("Date", "Open", "High", "Low", "Close", "Adj Close", "Volume"),
                data = convertedData
            ).build()

        ExcelSheetAdapter.outputExcelSheet(ws, "test.xlsx")
        assert(true)

    }

    @Test
    fun testSharpeCalculationAndConversionOutput() = runBlocking{
        val marketDataHandler = MarketDataHandler()

        val sharpeData = marketDataHandler.getSharpeRatio(historicalData)


    }
}