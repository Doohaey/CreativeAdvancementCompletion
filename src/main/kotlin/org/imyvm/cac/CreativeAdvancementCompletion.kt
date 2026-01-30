package org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.application.AdvancementProgressHandler
import org.imyvm.cac.entrypoint.commands.CommandManager
import org.imyvm.cac.entrypoint.listeners.PlayerListener
import org.imyvm.cac.domain.event.EventStatus
import org.imyvm.cac.entrypoint.tasks.AdvancementTickerTask
import org.imyvm.cac.util.LazyTicker
import org.imyvm.cac.util.i18n.Translator

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        setupCoreSystems()
        setupEntrypoint()

        Translator.tr("plugin.enabled")?.let { logger.info(it.toString()) }
    }

    private fun setupCoreSystems() {
        saveDefaultConfig()
        Translator.init(this)
        EventStatus.init(this)
        LazyTicker.start(this, config.getLong("ticking.interval", 20L))
    }

    private fun setupEntrypoint() {
        CommandManager(this).setup()
        server.pluginManager.registerEvents(PlayerListener(this), this)
        AdvancementTickerTask().register()
    }

    override fun onDisable() {
        LazyTicker.stop()
        Translator.tr("plugin.disabled")?.let { logger.info(it.toString()) }
    }
}