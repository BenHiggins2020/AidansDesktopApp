package com.ben.aidansdesktopapp.Adapter

import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.HistoricalDataRow
import com.ben.aidansdesktopapp.Repository.SharpeCalculator
import jdk.jfr.internal.Cutoff.INFINITY
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.random.Random
import kotlin.test.assertNotEquals

class MarketDataHandlerTest {

    private val testInstance = MarketDataHandler()
    private val historicalDataRow1 = HistoricalDataRow(
        "2023-05-20",
        "145.23",
        "147.90",
        "143.50",
        "146.80",
        "146.80",
        "1,550"
    )

    private val historicalDataRow2 = HistoricalDataRow(
        "2023-05-21",
        "155.23",
        "155.90",
        "78.50",
        "146.80",
        "150.80",
        "1,550"
    )

    private val historicalDataRow3 = HistoricalDataRow(
        "2023-06-22",
        "155.23",
        "155.90",
        "78.50",
        "146.80",
        "155.80",
        "1,550"
    )
    private val historicalDataRow4 = HistoricalDataRow(
        "2023-06-22",
        "155.23",
        "155.90",
        "78.50",
        "146.80",
        "158.20",
        "1,550"
    )

    private val historicalData = HistoricalData("BEN", listOf(
        historicalDataRow1,
        historicalDataRow2,
        historicalDataRow3,
        historicalDataRow4
    ))

    fun generateMockHistoricalData(symbol: String, numRows: Int = 100): HistoricalData {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.now().minusDays(numRows.toLong())

        val rows = List(numRows) { index ->
            val date = startDate.plusDays(index.toLong()).format(formatter)
            val open = Random.nextDouble(100.0, 500.0).toString()
            val high = Random.nextDouble(open.toDouble(), 550.0).toString()
            val low = Random.nextDouble(50.0, open.toDouble()).toString()
            val close = Random.nextDouble(low.toDouble(), high.toDouble()).toString()
            val adjClose = (close.toDouble() * 0.98).toString()
            val volume = Random.nextInt(100000, 500000).toString()

            HistoricalDataRow(date, open, high, low, close, adjClose, volume)
        }

        return HistoricalData(symbol, rows)
    }

    fun Collection<Double>.standardDeviation(): Double {
        val mean = this.average()

        var summation = 0.0

        this.forEach {
            summation += ( (it - mean) * (it - mean) )
        }

        val standardDeviation  = Math.sqrt(
            summation / this.size
        )

        return standardDeviation
    }

    @Test
    fun testOfflineCalculationFlow() = runBlocking{
        val historicalData = generateMockHistoricalData("BEN")
        val sharpeRatio = testInstance.getSharpeRatio(historicalData)
        println("Sharpe Ratio: $sharpeRatio")
        assert(sharpeRatio.isNotEmpty())
        assert(sharpeRatio.containsKey("BEN"))
        assert(sharpeRatio["BEN"]!! > 0.0)
    }

    @Test
    fun testCalculationForSharpeRatio() = runBlocking{


        val adjCloses = listOf<Double>(
            historicalDataRow1.adjClose.toDouble(),
            historicalDataRow2.adjClose.toDouble(),
            historicalDataRow3.adjClose.toDouble(),
            historicalDataRow4.adjClose.toDouble()
        )

        /*//Standard Deviation: calculation
        val mean = adjCloses.average()

        var summation = 0.0

        adjCloses.forEach {
            summation += ( (it - mean) * (it - mean) )
        }
        val standardDeviation  = Math.sqrt(
            summation / adjCloses.size
        )*/

        val sharpeRatio = testInstance.getSharpeRatio(historicalData)

        println("Sharpe Ratio: $sharpeRatio")
        val ratio = sharpeRatio["BEN"]!!

        assert(sharpeRatio.isNotEmpty())
        assert(sharpeRatio.containsKey("BEN"))
        assert(ratio > 0.0)

        assertNotEquals(
            actual = ratio.toString(),
            illegal = POSITIVE_INFINITY.toString()
        )

        assertNotEquals(
            actual = ratio.toString(),
            illegal = NEGATIVE_INFINITY.toString()
        )



    }
}