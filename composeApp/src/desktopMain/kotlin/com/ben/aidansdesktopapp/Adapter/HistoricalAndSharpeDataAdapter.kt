package com.ben.aidansdesktopapp.Adapter

import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.HistoricalDataRow

class HistoricalAndSharpeDataAdapter { //TODO: Rename to include SharpeRatio
    /*
    * This class will take the data provided from the MarketDataHandler
    * and convert it to a format to be output to an xml or csv file
    */

    companion object {

        fun convertHistoricalDataToExcelFormat(historicalData: HistoricalData): List<List<String>> {
            return convertHistoricalRowDataToExcelFormat(historicalData.rows)
        }

        fun convertHistoricalDataMapToExcelFormat(historicalDataMap: Map<String, HistoricalData>): Map<String, List<List<String>>> {
            return mapOf<String, List<List<String>>>(
                Pair(
                    historicalDataMap.keys.first(),
                    convertHistoricalRowDataToExcelFormat(historicalDataMap.values as List<HistoricalDataRow>)
                )
            )
        }

        fun convertHistoricalRowDataToExcelFormat(historicalDataRowList: List<HistoricalDataRow>): List<List<String>> {
            //Each historical data row should be its own list.

            //Sheet data will be all the data stored on the page.
            val sheetData = mutableListOf<List<String>>()

            historicalDataRowList.forEach { historicalDataRow ->
                val rows = mutableListOf<String>()
                rows.addAll(
                    listOf(
                        historicalDataRow.date,
                        historicalDataRow.open,
                        historicalDataRow.high,
                        historicalDataRow.low,
                        historicalDataRow.close,
                        historicalDataRow.adjClose,
                        historicalDataRow.volume
                    )
                )

                sheetData.add(rows.toList())
            }

            return sheetData.toList()

        }

        fun convertSharpeRatioDataToExcelFormat(sharpeRatioMap: Map<String, Double>): List<List<String>> {
            //it should look like this: <Ticker> <Ratio> for each list
            val ratioExcelData = mutableListOf<List<String>>()
            val row = mutableListOf<String>()

            sharpeRatioMap.forEach { (symbol, ratio) ->
                row.add(symbol)
                row.add(ratio.toString())
                ratioExcelData.add(row)
                row.clear()
            }
            return ratioExcelData
        }
    }


}