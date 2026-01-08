package org.imyvm.cac

import org.bukkit.plugin.java.JavaPlugin

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        logger.info("CreativeAdvancementCompletion enabled")
    }

    override fun onDisable() {
        logger.info("CreativeAdvancementCompletion disabled")
    }
}
