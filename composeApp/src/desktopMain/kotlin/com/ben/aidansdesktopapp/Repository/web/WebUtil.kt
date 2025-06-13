package com.ben.aidansdesktopapp.Repository.web

import java.time.Instant

class WebUtil {
    companion object {
        fun createUrlPeriod(years:Int):String {
            val today = Instant.now().epochSecond
            val yearsAgo = today - (years * 365 * 24 * 60 * 60) //x years worth of seconds)
            return "&period1=$yearsAgo&period2=$today"
        }
    }
}