package org.imyvm.cac.entrypoint.tasks

import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.application.GameDisplayHandler
import org.imyvm.cac.util.LazyTicker

class GameDisplayTask {

    fun register(plugin: JavaPlugin) {
        LazyTicker.registerTask { GameDisplayHandler.updateScoreboards(plugin) }
        LazyTicker.registerTask { GameDisplayHandler.updateBossBars() }
    }
}