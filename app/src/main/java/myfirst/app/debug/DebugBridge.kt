package myfirst.app.debug

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Мост между HTTP-сервером (фоновый поток) и игровым UI (Main).
 */
object DebugBridge {

    @Volatile
    var commandHandler: (suspend (String) -> String)? = null

    suspend fun execute(command: String): String {
        val handler = commandHandler
            ?: return "ERROR: game not ready"
        return withContext(Dispatchers.Main.immediate) {
            handler(command.trim())
        }
    }
}
