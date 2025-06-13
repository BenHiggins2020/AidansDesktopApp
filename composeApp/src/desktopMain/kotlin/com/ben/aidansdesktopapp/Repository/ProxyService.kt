package com.ben.aidansdesktopapp.Repository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
class ProxyService {
    companion object {
        fun startProxy(url: String) {
            embeddedServer(Netty, port = 8080) {
                routing {
                    get("/{symbol}/history") {
                        val symbol =
                            call.parameters["symbol"] ?: "AAPL" // Default to AAPL if missing
                        val yahooUrl = url

                        val client = HttpClient()

                        val response: String = client.get(yahooUrl) {
                            header("User-Agent", "Mozilla/5.0 (Windows NT 10.0)")
                            header("Accept-Language", "en-US,en;q=0.9")
                            header("Referer", "https://www.google.com/")
                        }.bodyAsText()

                        call.respondText(response) // Forward response to client
                    }
                }
            }.start(wait = true)
        }
    }
}