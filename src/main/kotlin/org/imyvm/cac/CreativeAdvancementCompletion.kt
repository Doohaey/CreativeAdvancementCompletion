package org.imyvm.cac.org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.org.imyvm.cac.api.commands.CommandManager
import org.imyvm.cac.org.imyvm.cac.api.listeners.PlayerListener
import org.imyvm.cac.i18n.Translator

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
        Translator.init(this)
        CommandManager(this).setup()
        server.pluginManager.registerEvents(PlayerListener(this), this)
        Translator.tr("plugin.enabled")?.let { logger.info(it.toString()) }
    }

    override fun onDisable() {
        Translator.tr("plugin.disabled")?.let { logger.info(it.toString()) }
    }
}