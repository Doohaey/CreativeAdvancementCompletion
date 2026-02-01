package org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.entrypoint.commands.CommandManager
import org.imyvm.cac.entrypoint.listeners.PlayerListener
import org.imyvm.cac.domain.event.EventStatus
import org.imyvm.cac.domain.repository.EventRepository
import org.imyvm.cac.entrypoint.tasks.AdvancementTickerTask
import org.imyvm.cac.entrypoint.tasks.GameDisplayTask
import org.imyvm.cac.util.LazyTicker
import org.imyvm.cac.util.i18n.Translator

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        setupCoreSystems()
        setupEntrypoint()

        EventRepository.load(this)
        server.scheduler.runTaskTimerAsynchronously(this, Runnable {
            EventRepository.save(this)
        }, 6000L, 6000L)

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
        GameDisplayTask().register(this)
    }

    override fun onDisable() {
        EventRepository.save(this)
        LazyTicker.stop()

        Translator.tr("plugin.disabled")?.let { logger.info(it.toString()) }
    }
}