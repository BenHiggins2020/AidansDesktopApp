package com.ben.aidansdesktopapp.Repository

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