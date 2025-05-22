package com.ben.aidansdesktopapp.Repository

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WebService {

    private val mostActiveUrl = "https://finance.yahoo.com/markets/stocks/most-active/"
    private val wikiSNPCompanies = "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies"
    private val historicalDataUrl = "https://finance.yahoo.com/quote/%s/history?frequency=1mo"
    private val TEN_SECOND_MILIS = 10000

    private lateinit var symbols: List<String>
    private lateinit var tableMap: Map<String, TableData>

    private fun createDocument(url: String): Document {
        return Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .timeout(TEN_SECOND_MILIS)
            .get()
    }

    private fun parseMostActive(): Map<String, TableData> {
        val tableMap = mutableMapOf<String, TableData>()

        try {
            val doc: Document = Jsoup.connect(mostActiveUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(TEN_SECOND_MILIS)
                .get()

            val table: Elements = doc.select("table")
            val rows = table.select("tr")
            val header = rows.first()
            val headerCells = header?.select("th")
            println(headerCells?.joinToString(" | ") { it.text() })

            rows.forEach {
                val cells = it.select("td")

                if (cells.isEmpty()) {
                    return@forEach
                }

                val tableData = TableData(
                    symbol = cells[0].text(),
                    name = cells[1].text(),
                    price = cells[3].text(),
                    change = cells[4].text(),
                    changePercent = cells[5].text(),
                    volume = cells[6].text(),
                )

                tableMap[tableData.symbol] = tableData

                println(cells.joinToString(" | ") { it.text() })
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return tableMap
    }

    private fun parseSP500Symbols(): List<String> {
        var symbols = mutableListOf<String>()

        try {
            val doc = createDocument(wikiSNPCompanies)
            val table: Elements = doc.select("table")
            val SnPTable = table.first() ?: return symbols

            val rows = SnPTable.select("tr").apply {
                this.first()?.remove()
            }

            rows.forEach {
                val cells = it.select("td")
                if (cells.isEmpty() || cells.first.text().isNullOrEmpty()) {
                    println("Empty row")
                    return@forEach
                }
                symbols.add(cells[0].text())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return symbols.toList()
    }

    fun getMostActive(): Map<String, TableData> {
        if (!::tableMap.isInitialized) {
            tableMap = parseMostActive()
        }
        return tableMap
    }

    fun getSnP500Symbols(): List<String> {
        if (!::symbols.isInitialized) {
            symbols = parseSP500Symbols()
        }
        return symbols
    }

    fun createHistoricalDataUrlFor5YPeriod(symbol: String): String {
        //Current date in UNIXTimestamp
        val today = Instant.now().epochSecond
        val fiveYearsAgo = today - (5 * 365 * 24 * 60 * 60) //5 years worth of seconds

        return historicalDataUrl.format(symbol, symbol) + "&period1=$fiveYearsAgo&period2=$today"
    }

    fun parseHistoricalData(symbol: String): List<HistoricalDataRow> {
        val url = createHistoricalDataUrlFor5YPeriod(symbol)
        val doc = createDocument(url)
        val table: Elements = doc.select("table")

        val rows = table.select("tr").apply {
            this.removeAt(0)
        }

        val historicalDataList = mutableListOf<HistoricalDataRow>()

        rows.forEach {
            val cells = it.select("td")

            if (cells.isEmpty() || cells.size < 7) {
                return@forEach
            }
            val data = HistoricalDataRow(
                date = cells[0].text(),
                open = cells[1].text(),
                high = cells[2].text(),
                low = cells[3].text(),
                close = cells[4].text(),
                adjClose = cells[5].text(),
                volume = cells[6].text(),
            )
            historicalDataList.add(data)
        }

        return historicalDataList.toList()
    }

    fun createHistoricalData(symbol: String): HistoricalData {
        val list = parseHistoricalData(symbol)
        return HistoricalData(symbol, list)
    }
}