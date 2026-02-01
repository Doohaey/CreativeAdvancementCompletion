package org.imyvm.cac.util.i18n

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.Properties

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
        val nextLanguage = configured.ifBlank { DEFAULT_LANGUAGE }

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

        val resourceStream = plugin.getResource(path) ?: return null

        return try {
            val props = Properties()
            resourceStream.bufferedReader(StandardCharsets.UTF_8).use { reader ->
                props.load(reader)
            }
            props
        } catch (e: Exception) {
            plugin.logger.severe("Failed to load language file $path: ${e.message}")
            null
        }
    }
}

