package org.imyvm.cac.event

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileWriter
import java.time.Instant

/**
 * Manages the active/inactive status of the creative advancement event.
 *
 * - Status is stored in config.yml under "event.active".
 * - Every start/stop is appended as a JSON line to "event_log.jsonl" in the plugin data folder.
 */
object EventStatus {

    private lateinit var plugin: JavaPlugin

    @Volatile
    private var active: Boolean = false

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        this.active = plugin.config.getBoolean("event.active", false)
    }

    fun isActive(): Boolean = active

    /**
     * Sets the event to active/inactive.
     * If the value changes, it is persisted and logged.
     */
    fun setActive(value: Boolean, actor: String?) {
        if (!::plugin.isInitialized) return
        if (active == value) return

        active = value

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

