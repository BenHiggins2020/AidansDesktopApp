package com.ben.aidansdesktopapp.Repository

import kotlin.math.sqrt

class SharpeCalculator {

    /* Aidan's Algorithm:
    *   get the monthly values AdjClose
    *   Calculate Monthly returns Array:
    *       Create an array of values "Monthly returns"
    *       Each element in the array is the ratio of currentMonth (Adj Close) / previousMonth (Adj Close) -1
    *
    *
    *   Calculate the Sharpe Ratio: (riskRate = 0.0009)
    *       mean(monthlyReturns) - riskRate / std(monthlyReturns)
    */


    fun runCalculation(historicalData: HistoricalData):Double {
        val monthlyReturns = calculateMonthlyReturns(historicalData.rows)
        val sharpeRatio = calculateSharpeRatio(monthlyReturns, RISK_RATE)
        println("Sharpe Ratio: $sharpeRatio")
        return sharpeRatio
    }

    private fun calculateMonthlyReturns(data: List<HistoricalDataRow>): List<Double> {
        val monthlyReturns = mutableListOf<Double>()
        println("Data: $data")
        for (i in 1 .. data.size-1) {
            val currentMonth = data[i]
            val previousMonth = data[i - 1]

            val currentAdjClose = currentMonth.adjClose.toDouble()
            val previousAdjClose = previousMonth.adjClose.toDouble()

            val monthlyReturn = (currentAdjClose - previousAdjClose) / previousAdjClose
            println("monthlyReturn: $monthlyReturn")
            monthlyReturns.add(monthlyReturn)
        }
        println("Monthly Returns: $monthlyReturns")

        return monthlyReturns
    }

    private fun calculateSharpeRatio(monthlyReturns: List<Double>, riskRate: Double): Double {
        val mean = monthlyReturns.average()
        val std = monthlyReturns.standardDeviation()
        println("Standard Deviation: $std , mean: $mean , calculation: ${(mean - riskRate)/std}")
        return (mean - riskRate) / std
    }

    companion object {
        val RISK_RATE = 0.0009
    }


}