package org.imyvm.cac

import com.mojang.brigadier.Command
import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.commands.CommandManager

class CreativeAdvancementCompletion : JavaPlugin() {
    override fun onEnable() {
        CommandManager(this).setup()
        logger.info("CreativeAdvancementCompletion enabled")
    }

    override fun onDisable() {
        logger.info("CreativeAdvancementCompletion disabled")
    }
}
