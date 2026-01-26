package org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.api.commands.CommandManager

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        CommandManager(this).setup()
        logger.info("CreativeAdvancementCompletion enabled")
    }

    override fun onDisable() {
        logger.info("CreativeAdvancementCompletion disabled")
    }
}
