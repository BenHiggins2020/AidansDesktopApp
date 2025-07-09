package com.ben.aidansdesktopapp.Presentation.components

import org.jetbrains.annotations.TestOnly

/**
 * Represents a table with rows and columns.
 * The index for each row will be the lenght of the columns.
 * The columns can be traversed by their number or index.
 * Each time an item is added to a row who has the maximum length, a new empty cell must be added to the rest of the rows.
 *
 */
class Table() {
    val rows = mutableListOf<MutableList<String>>()

    fun getCell(column: Int?, row: Int? = null): Any {
        if (row == null && column == null) return "FAILED"

        if (row == null) { // get entire column
            val columnList = mutableListOf<String>()
            rows.forEach {
                columnList.add(it.getOrElse(column!!, { "" }))
            }
        }
        if (column == null) { // get entire row!
            return rows.getOrElse(row!!, { "" })
        }

        val row = rows.getOrElse(row!!, { "" }) as Array<*>
        val cell = row.getOrElse(column!!, { "" })!!
        return cell
    }


    fun addRow(row: MutableList<String>) {
        rows.add(row)
    }

    fun set(column: Int, row: Int, value: String) {
        if (row == null && column == null) return

        if (row == null) { // get entire column
            val columnList = mutableListOf<String>()

            rows.forEach {
                columnList.add(it.getOrElse(column!!, { "" }))
            }
        }
        if (column == null) { // set entire row! (we cant do this now)

        }

        //if row is too small...
        if(rows.get(row).size < column){
            val diff = column - rows.get(row).size
            for(i in 0..diff){
                rows.get(row).add("")
            }
        }
        rows.get(row).set(column,value)
    }
}


