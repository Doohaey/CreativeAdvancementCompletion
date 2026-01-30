package org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.entrypoints.commands.CommandManager
import org.imyvm.cac.entrypoints.listeners.PlayerListener
import org.imyvm.cac.domain.event.EventStatus
import org.imyvm.cac.util.LazyTicker
import org.imyvm.cac.util.i18n.Translator

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
        Translator.init(this)
        EventStatus.init(this)

        CommandManager(this).setup()
        server.pluginManager.registerEvents(PlayerListener(this), this)

        LazyTicker.start(this, this.config.getLong("ticking.interval", 20L))

        Translator.tr("plugin.enabled")?.let { logger.info(it.toString()) }
    }

    override fun onDisable() {
        Translator.tr("plugin.disabled")?.let { logger.info(it.toString()) }
    }
}