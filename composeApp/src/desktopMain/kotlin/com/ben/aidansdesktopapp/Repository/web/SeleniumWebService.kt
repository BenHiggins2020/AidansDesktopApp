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

import com.ben.aidansdesktopapp.Repository.util.WebUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class SeleniumWebService {


    fun api(symbol: String) {

        val baseUrl = openYahooFinanceToSymbol(symbol)
        //https://finance.yahoo.com/quote/PODD/history/?frequency=1mo&period1=1592100166&period2=1749866555
        val historicalDataUrl = baseUrl + "/history?frequency=1mo"+ WebUtil.createUrlPeriod(5)
        val driver = createChromeDriver()
//        driver.get("https://finance.yahoo.com/quote/$symbol/history")
//        driver.navigate().to(historicalDataUrl)
        driver.get(historicalDataUrl)
        println("Open successful, ")

//        println("Navigation successful, ")

        val parser = SeleniumParser(driver)
        //Parse table exports data at current page
        parser.parseForTable().forEach {
            println(it)
        }


        driver.quit()

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
        val chromeDriverPath = "C:\\Users\\Ben\\AndroidStudioProjects\\AidansDesktopApp\\composeApp\\chrome\\chromedriver.exe"
        System.setProperty("webdriver.chrome.driver", chromeDriverPath)
        val options = ChromeOptions()
//        options.setBinary(chromeBinaryPath)
        options.addArguments("--disable-gpu")
//        options.addArguments("--headless=new") // comment out if you want to see the browser
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        return ChromeDriver(options)
    }

    private inner class SeleniumParser(
        private val driver: WebDriver
    ) {
        fun parseForTable(): List<String> {
            return try {
                val table = driver.findElement(By.tagName("table"))
                println("Searching elements. ${table}")
                println("Table details: ${table.size}")
                val rows = table.findElements(By.tagName("tr"))
                println("Rows: ${rows.size} ")
                val adjCloseData = mutableListOf<String>()
                rows.forEach {
                    if(it.text.contains("dividend")) return@forEach
//                    println("Row: ${it.text}")
                    it.findElements(By.tagName("td")).forEachIndexed { index, webElement ->
                        when (index) {
//                            0 -> println("Date: ${webElement.text}")
//                            1 -> println("Open: ${webElement.text}")
//                            2 -> println("High: ${webElement.text}")
//                            3 -> println("Low: ${webElement.text}")
//                            4 -> println("Close: ${webElement.text}")
                            5 -> adjCloseData.add(webElement.text)//.also {println("Adj Close: ${webElement.text}")}
//                            6 -> println("Volume: ${webElement.text}")
                        }
                    }
                }

                return adjCloseData //TODO: Update this.. But we really just want to output the tables??
                //really just want to calculate all the sharpe data...

            } catch (e: Exception) {
                println("Failed to search for my element: ${e.stackTraceToString()}")
                driver.quit()
                return emptyList()
            }
        }
    }
}
