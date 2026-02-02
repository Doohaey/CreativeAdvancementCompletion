package org.imyvm.cac.domain.event

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileWriter
import java.time.Instant

object EventStatus {

    private lateinit var plugin: JavaPlugin

    @Volatile
    private var active: Boolean = false
    var currentSessionId: String? = null
        private set

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        this.active = plugin.config.getBoolean("event.active", false)
        this.currentSessionId = plugin.config.getString("event.session_id")
    }

    fun isActive(): Boolean = active

    fun setActive(value: Boolean, actor: String?) {
        if (!::plugin.isInitialized) return
        if (active == value) return

        active = value
        if (active) {
            currentSessionId = "session_${System.currentTimeMillis()}"
            plugin.config.set("event.session_id", currentSessionId)
        } else {
            currentSessionId = null
        }

        plugin.config.set("event.active", active)
        plugin.saveConfig()

        logChange(value, actor)
    }

    private fun logChange(active: Boolean, actor: String?) {
        val folder = plugin.dataFolder
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folder, "event_log.jsonl")
        val timestamp = Instant.now().toString()
        val status = if (active) "start" else "stop"
        val safeActor = (actor ?: "console").replace("\"", "\\\"")

        val jsonLine =
            """{"timestamp":"$timestamp","status":"$status","actor":"$safeActor"}"""

        FileWriter(file, true).use { writer ->
            writer.appendLine(jsonLine)
        }
    }
}

