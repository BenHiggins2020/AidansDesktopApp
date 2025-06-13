/*
package com.ben.aidansdesktopapp.Repository.web

import io.github.bonigarcia.wdm.WebDriverManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.IOException
import java.net.URLEncoder
import java.time.Duration

class SeleniumWebService {


    val baseUrl = "https://query1.finance.yahoo.com/v7/finance/download/"
    lateinit var cookies: List<String>
    lateinit var crumb: String

    //This is the entry point for the class...
    fun extractDataToCSV(symbol: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(createUrlWithCookies(symbol))
            .addHeader("Cookie", cookies.joinToString("; "))
            .header("User-Agent", "Mozilla/5.0")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error: response was not successful: Response Code ${response.code}")
                return
            }
            val body = response.body?.string() ?: return
            println(body)
            println("CSV output: \n$body")
        }
    }

    private fun createUrlWithCookies(symbol: String): String {
        if (!::cookies.isInitialized || !::crumb.isInitialized) {
            getYahooCrumbAndCookies(symbol).also {
                crumb = it.first
                cookies = it.second
            }
        }

        return try {
            val url =
                baseUrl + symbol + WebUtil.Companion.createUrlPeriod(5) + "&interval=1mo&events=history&crumb=${
                    URLEncoder.encode(
                        crumb,
                        "UTF-8"
                    )
                }"
            return url
        } catch(e:Exception) {
            e.printStackTrace()
            return ""
        }

    }

    //This is a helper function which can extract the crumb and cookies from the webpage.
    private fun getYahooCrumbAndCookies(symbol: String): Pair<String, List<String>> {
        val options = ChromeOptions().apply {
//            setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe")
            addArguments("--start-maximized")
//            addArguments("--headless=new")  // <- Use the new headless mode
            addArguments("--no-sandbox")    // <- Avoid crashes in some environments
            addArguments("--disable-dev-shm-usage") // <- Prevent Chrome from using shared memory
            addArguments("--remote-allow-origins=*") // <- Avoid CORS issue with remote debugging
        }

        WebDriverManager.chromedriver().setup()

        val driver = ChromeDriver(options)
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30))
        val url = "https://finance.yahoo.com/quote/$symbol/history"
        println("Going to url: $url")
        driver.get(url)
        Thread.sleep(5000) // This should give JS some time to render the "Crumb"

        val mCookies = driver.manage().cookies
        mCookies.forEach {
            println("cookie ${it.name}=${it.value}" )
        }
        val cookieHeader = mCookies.joinToString("; ") { "${it.name}=${it.value}" }
        val mCrumb = (driver as JavascriptExecutor).executeScript("return window.YAHOO.Finance.ContextStore.crumb;") as? String
        println("Cookie header: $cookieHeader")
        println("Crumb: $mCrumb")
        //New Idea:
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Cookie", cookieHeader)
            .addHeader("User-Agent", "Mozilla/5.0")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val csvData = response.body?.string()
            println(csvData)
        }

        return crumb to cookies
        //Looking for driver in page source
        val source = driver.pageSource
        val crumbRegex = Regex(""""CrumbStore":\{"crumb":"(.*?)"}""")
        println("Source: $source")
        val crumbResult = crumbRegex.find(source)
        println("Crumb result: $crumbResult")
        val crumb = crumbResult?.groups?.get(1)?.value?.replace("\\u002F", "/") ?: ""
        println("Crumb: $crumb")

        //Extract cookies
        val cookies = driver.manage().cookies.map { "${it.name}=${it.value}" }
        driver.quit()
        return crumb to cookies

    }

}*/
package com.ben.aidansdesktopapp.Repository.web

import okhttp3.OkHttpClient
import okhttp3.Request
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class SeleniumWebService {

    fun api(symbol: String){

        val driver = openYahooFinanceToSymbol(symbol)
        println("Open successful, ")

        driver.navigate().to("https://finance.yahoo.com/quote/$symbol/history")

        println("Navigation successful, ")

        try {
            val table = driver.findElement(By.tagName("table"))
            println("Searching elements. ${table}")
            println("Table details: ${table.size}")
            val rows = table.findElements(By.tagName("tr"))
            println("Rows: ${rows.size} ")
            rows.forEach {
                println("Row: ${it.text}")
                it.findElements(By.tagName("td")).forEachIndexed { index, webElement ->
                    when(index){
                        0 -> println("Date: ${webElement.text}")
                        1 -> println("Open: ${webElement.text}")
                        2 -> println("High: ${webElement.text}")
                        3 -> println("Low: ${webElement.text}")
                        4 -> println("Close: ${webElement.text}")
                        5 -> println("Adj Close: ${webElement.text}")
                        6 -> println("Volume: ${webElement.text}")
                    }
                }
            }
//            println("Table text: ${table.text}")

        } catch (e:Exception){
            println("Failed to search for my elemtn: ${e.stackTraceToString()}")
            driver.quit()
        }

        driver.quit()

    }

    fun openBrowser(url:String): WebDriver {
        val driver = createChromeDriver()
        driver.get(url)
        return driver
    }
    fun openYahooFinanceToSymbol(symbol:String): WebDriver {
        val url = "https://finance.yahoo.com/quote/$symbol"
        return openBrowser(url)
    }

    fun extractDataToCSV(ticker: String): String? {
        val driver = createChromeDriver()
        return try {
            val url = "https://finance.yahoo.com/quote/$ticker/history"
            println("Opening $url")
            driver.get(url)
            println("Driver.get(url) called -> PageSource = ${driver.pageSource}")
            driver.pageSource

            // Wait up to 10 seconds for crumb to appear
//            val crumb = waitForCrumb(driver) ?: throw RuntimeException("Failed to get crumb.")
//            val crumb = waitForCrumb2(driver) ?: throw RuntimeException("Failed to get crumb.")

            val crumb = "k7rDWxxoYP0"

            val cookies = driver.manage().cookies
            val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }

            println("Crumb: $crumb")
            println("Cookies: $cookieHeader")

            // Generate timestamps for period1 and period2
            val now = Instant.now().epochSecond
            val oneYearAgo = now - (60L * 60 * 24 * 365)

            val downloadUrl =
                "https://query1.finance.yahoo.com/v7/finance/download/$ticker?period1=$oneYearAgo&period2=$now&interval=1d&events=history&crumb=$crumb"
            val downloadUrl2 = "https://query1.finance.yahoo.com/v7/finance/quote?&symbols=${ticker}&fields=currency,fromCurrency,toCurrency,exchangeTimezoneName,exchangeTimezoneShortName,gmtOffSetMilliseconds,regularMarketChange,regularMarketChangePercent,regularMarketPrice,regularMarketTime,preMarketChange,preMarketChangePercent,preMarketPrice,preMarketTime,priceHint,postMarketChange,postMarketChangePercent,postMarketPrice,postMarketTime,extendedMarketChange,extendedMarketChangePercent,extendedMarketPrice,extendedMarketTime,overnightMarketChange,overnightMarketChangePercent,overnightMarketPrice,overnightMarketTime&crumb=${crumb}}&formatted=false&region=US&lang=en-US"

            println("Downloading from: $downloadUrl2")

            fetchCSV(downloadUrl2, cookieHeader)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            driver.quit()
        }
    }

    private fun waitForCrumb2(driver: WebDriver, timeoutSec: Int = 10): String? {
        val executor = (driver as JavascriptExecutor)
        WebDriverWait(driver, Duration.ofSeconds(10)).until {
            executor.executeScript("return document.readyState") == "complete"
        }
        println("WebDriver wait finished, page has loaded")
        val crumb = executor.executeScript("return window.YAHOO && window.YAHOO.Finance && window.YAHOO.Finance.ContextStore ? window.YAHOO.Finance.ContextStore.crumb : null;")

        return crumb.toString()
    }
    private fun waitForCrumb(driver: WebDriver, timeoutSec: Int = 10): String? {
        val executor = driver as JavascriptExecutor
        val start = System.currentTimeMillis()
        while ((System.currentTimeMillis() - start) < timeoutSec * 1000) {
            try {
                val crumb = executor.executeScript("return window.YAHOO.Finance.ContextStore.crumb;").also {
                    println("also ... ${it}")
                }
                if (crumb != null && crumb is String) {
                    return crumb
                }
                Thread.sleep(500)
            } catch (e: Exception) {
                println("Failed to get crumb. Too bad!! \n ${e.stackTraceToString()}")
                Thread.sleep(500)
            }
        }
        return null
    }

    private fun fetchCSV(url: String, cookieHeader: String): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Cookie", cookieHeader)
            .addHeader("User-Agent", "Mozilla/5.0")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code ${response.code}")
            return response.body?.string() ?: throw IOException("Empty body")
        }
    }

    private fun createChromeDriver(): WebDriver {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe")
        val chromeBinaryPath = "chrome/chrome.exe"
        val chromeDriverPath = "chrome/chromedriver.exe"
//        System.setProperty("webdriver.chrome.driver", chromeDriverPath)
        val options = ChromeOptions()
//        options.setBinary(chromeBinaryPath)
        options.addArguments("--disable-gpu")
//        options.addArguments("--headless=new") // comment out if you want to see the browser
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        return ChromeDriver(options)
    }
}
