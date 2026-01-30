package org.imyvm.cac.entrypoint.tasks

import org.bukkit.Bukkit
import org.imyvm.cac.application.AdvancementProgressControlHandler
import org.imyvm.cac.util.LazyTicker

class AdvancementTickerTask {

    fun register() {
        LazyTicker.registerTask {
            Bukkit.getOnlinePlayers().forEach { player ->
                AdvancementProgressControlHandler.performDeepCleanup(player)
            }
        }
    }
}