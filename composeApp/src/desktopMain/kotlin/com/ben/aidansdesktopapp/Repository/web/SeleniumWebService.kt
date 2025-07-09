package com.ben.aidansdesktopapp.Repository.web

import com.ben.aidansdesktopapp.Repository.HistoricalData
import com.ben.aidansdesktopapp.Repository.HistoricalDataRow
import com.ben.aidansdesktopapp.Repository.mutableHistoricalDataRow
import com.ben.aidansdesktopapp.Repository.toHistoricalData
import com.ben.aidansdesktopapp.Repository.toHistoricalDataRow
import com.ben.aidansdesktopapp.Repository.util.WebUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class SeleniumWebService {

   suspend fun api(symbol: String): HistoricalData {
       return withContext(Dispatchers.Default){
        val baseUrl = openYahooFinanceToSymbol(symbol)
        //Example:
        //https://finance.yahoo.com/quote/PODD/history/?frequency=1mo&period1=1592100166&period2=1749866555
        val historicalDataUrl = baseUrl + "/history?frequency=1mo" + WebUtil.createUrlPeriod(5)
        val driver = createChromeDriver()

        println("Opening: $historicalDataUrl")
        val executor = driver as JavascriptExecutor
       driver.get(historicalDataUrl)

       WebDriverWait(driver, Duration.ofSeconds(30)).until{
            executor.executeScript("return document.readyState") == "complete"
       }

        println("Open successful, ")
        val parser = SeleniumParser(driver)

        //Parse table exports data at current page
        val parserData = parser.parseForTable()
        println("Parse complete for: $symbol")

        /*val adjCloseData = parserData.first.map {
            it.toFloat()
        }*/

        println("Conversion complete for: $symbol")

        val historicalData = parserData.second
       println("Data received for historicalData: ${historicalData.size}")

        driver.quit()
        println("Data extraction complete for: $symbol")

        return@withContext historicalData.toHistoricalData(symbol)
       }
    }

    private fun openYahooFinanceToSymbol(symbol: String): String {
        val url = "https://finance.yahoo.com/quote/$symbol"
        return url
    }


    private fun createChromeDriver(): WebDriver {
        System.setProperty(
            "webdriver.chrome.driver",
            "C:\\Tools\\chromedriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"
        )
        val chromeDriverPath =
            "C:\\Users\\Ben\\AndroidStudioProjects\\AidansDesktopApp\\composeApp\\chrome\\chromedriver.exe"

        System.setProperty("webdriver.chrome.driver", chromeDriverPath)
        System.setProperty("http.keepAlive", "false");
        val options = ChromeOptions()
//        options.setBinary(chromeBinaryPath)
//        options.addArguments("--disable-gpu")
//        options.addArguments("--headless") // comment out if you want to see the browser
//        options.addArguments("--no-sandbox")
//        options.addArguments("--disable-dev-shm-usage")
        return ChromeDriver(options)
    }

    private inner class SeleniumParser(
        private val driver: WebDriver
    ) {
        fun parseForTable(): Pair<List<String>, List<HistoricalDataRow>> {
            return try {
                val table = driver.findElement(By.tagName("table"))
                println("Table details: ${table.size}")
                val rows = table.findElements(By.tagName("tr"))
                println("Rows: ${rows.size} ")

                val adjCloseData = mutableListOf<String>()
                val dataRow = mutableHistoricalDataRow()
                val dataRows = mutableListOf<HistoricalDataRow>()

                rows.forEach {
                    if (it.text.contains("dividend")) return@forEach
//                    println("Row: ${it.text}")
                    it.findElements(By.tagName("td")).forEachIndexed { index, webElement ->
                        when (index) {
                            0 -> {
                                dataRow.date = webElement.text
                            }
                            1 -> dataRow.open = webElement.text //println("Open: ${webElement.text}")
                            2 -> dataRow.high = webElement.text //println("High: ${webElement.text}")
                            3 -> dataRow.low = webElement.text //println("Low: ${webElement.text}")
                            4 -> dataRow.close = webElement.text //println("Close: ${webElement.text}")
                            5 -> {
                                dataRow.adjClose = webElement.text
                                adjCloseData.add(webElement.text)
                                //println("Adj Close: ${webElement.text}")
                            }
                            6 -> dataRow.volume = webElement.text //println("Volume: ${webElement.text}")
                        }
                    }
                    dataRows.add(dataRow.toHistoricalDataRow())
                }
                println("Got a bunch of data = ${adjCloseData.size} and ${dataRows.size}")
                return adjCloseData to dataRows //TODO: Update this.. But we really just want to output the tables??
                //really just want to calculate all the sharpe data...

            } catch (e: Exception) {
                println("Failed to search for my element: ${e.stackTraceToString()}")
                driver.quit()
                throw e
                return emptyList<String>() to emptyList<HistoricalDataRow>()
            }
        }
    }
}
