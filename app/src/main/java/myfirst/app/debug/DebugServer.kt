package myfirst.app.debug

import android.util.Log
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.net.NetworkInterface

class DebugServer(private val port: Int = DEFAULT_PORT) {

    private var server: EmbeddedServer<*, *>? = null

    val isRunning: Boolean
        get() = server != null

    fun start() {
        if (server != null) return

        server = embeddedServer(CIO, port = port, host = "0.0.0.0") {
            routing {
                get("/") {
                    call.respondText(HELP, ContentType.Text.Plain)
                }
                get("/state") {
                    val result = DebugBridge.execute("get state")
                    call.respondText(result, ContentType.Text.Plain)
                }
                get("/cmd") {
                    val cmd = call.request.queryParameters["c"]
                        ?: call.request.queryParameters["cmd"]
                    if (cmd.isNullOrBlank()) {
                        call.respondText(
                            "Missing ?c= or ?cmd=",
                            status = HttpStatusCode.BadRequest
                        )
                    } else {
                        val result = DebugBridge.execute(cmd)
                        call.respondText(result, ContentType.Text.Plain)
                    }
                }
                post("/cmd") {
                    val body = call.receiveText()
                    if (body.isBlank()) {
                        call.respondText(
                            "Empty body. Send command as plain text.",
                            status = HttpStatusCode.BadRequest
                        )
                    } else {
                        val result = DebugBridge.execute(body)
                        call.respondText(result, ContentType.Text.Plain)
                    }
                }
            }
        }.start(wait = false)

        Log.i(TAG, "Debug server started on port $port")
    }

    fun stop() {
        server?.stop(1000, 2000)
        server = null
        Log.i(TAG, "Debug server stopped")
    }

    companion object {
        const val DEFAULT_PORT = 8765
        private const val TAG = "DebugServer"

        private val HELP = """
            My First App — Debug Server
            ============================
            GET  /              this help
            GET  /state         current game state
            GET  /cmd?c=...     run command
            POST /cmd           body = command

            Commands:
              get state
              set money <number>
              add money <number>
              set base <number>
              set multiplier <number>
              set cooldown <ms>
              set crit <0..1>
              set hold <number>
              help
            """.trimIndent()

        fun findLocalIpAddresses(): List<String> {
            val result = mutableListOf<String>()
            try {
                val interfaces = NetworkInterface.getNetworkInterfaces() ?: return result
                for (iface in interfaces) {
                    if (!iface.isUp || iface.isLoopback) continue
                    for (addr in iface.inetAddresses) {
                        val host = addr.hostAddress ?: continue
                        if (host.contains(':')) continue // skip IPv6 for simplicity
                        if (host.startsWith("127.")) continue
                        result += host
                    }
                }
            } catch (_: Exception) {
            }
            return result
        }
    }
}
