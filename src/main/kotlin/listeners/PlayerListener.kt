package org.imyvm.cac.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.imyvm.cac.CreativeAdvancementCompletion

class PlayerListener(
    private val plugin: CreativeAdvancementCompletion
) {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendMessage("1")
    }
}