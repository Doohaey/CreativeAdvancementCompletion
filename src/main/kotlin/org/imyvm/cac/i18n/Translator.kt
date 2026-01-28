package org.imyvm.cac.i18n

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.Properties

/**
 * Simple i18n translator modeled after the reference project:
 * - Loads `lang/<language>.properties` from plugin resources
 * - Formats via MessageFormat
 * - Parses legacy color codes (& / §) and supports '\n'
 */
object Translator {

    private const val DEFAULT_LANGUAGE = "en_US"

    @Volatile
    private var plugin: JavaPlugin? = null

    @Volatile
    private var languageId: String = DEFAULT_LANGUAGE

    @Volatile
    private var bundle: Properties = Properties()

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        reload()
    }

    fun reload() {
        val plugin = this.plugin ?: return
        val configured = plugin.config.getString("language")?.trim().orEmpty()
        val nextLanguage = if (configured.isNotBlank()) configured else DEFAULT_LANGUAGE

        languageId = nextLanguage
        bundle = loadLanguage(nextLanguage) ?: loadLanguage(DEFAULT_LANGUAGE) ?: Properties()
    }

    fun send(receiver: CommandSender, key: String, vararg args: Any?) {
        tr(key, *args)?.let { component ->
            if (receiver is Player) receiver.sendMessage(component) else receiver.sendMessage(component.toString())
        }
    }

    fun tr(key: String?, vararg args: Any?): Component? {
        val raw = key?.let { bundle.getProperty(it) } ?: return null
        val formatted = if (args.isNotEmpty()) MessageFormat.format(raw, *args) else raw
        return TextParser.parse(formatted)
    }

    fun language(): String = languageId

    private fun loadLanguage(languageId: String): Properties? {
        val plugin = this.plugin ?: return null
        val path = "lang/$languageId.properties"
        val stream = plugin.getResource(path) ?: return null
        stream.use { input ->
            val props = Properties()
            InputStreamReader(input, StandardCharsets.UTF_8).use { reader ->
                props.load(reader)
            }
            return props
        }
    }
}

