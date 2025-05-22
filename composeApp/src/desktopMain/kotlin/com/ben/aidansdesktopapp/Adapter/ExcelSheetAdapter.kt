package com.ben.aidansdesktopapp.Adapter

import org.apache.poi.ss.util.AreaReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ExcelSheetAdapter {
    /*Use ExcelSheetAdapter to create excel sheets from data modified by HistoricalDataAdapter*/

    companion object {

        fun outputExcelSheet(workbook:XSSFWorkbook, fileName:String){
            FileOutputStream(fileName).use { outputStream -> workbook.write(outputStream) }
            workbook.close()
        }

        class ExcelSheetBuilder{
            private val workbook = XSSFWorkbook()

            fun build(): XSSFWorkbook {
                return workbook
            }

            fun withSheet(sheetName: String, headers:List<String>,data:List<List<String>>): ExcelSheetBuilder {
                val sheet = workbook.createSheet(sheetName)
                val headerRow = sheet.createRow(0)

                headers.forEachIndexed { index, header ->
                    val cell = headerRow.createCell(index)
                    cell.setCellValue(header)
                }

                //Creates data where each list is a row
                data.forEachIndexed { rowIndex, rowData ->
                    val row = sheet.createRow(rowIndex + 1)
                    rowData.forEachIndexed { cellIndex, cellValue ->
                        val cell = row.createCell(cellIndex)
                        cell.setCellValue(cellValue)
                    }
                }
                return this@ExcelSheetBuilder
            }

        }
    }

}