package org.imyvm.cac.api.listeners

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.imyvm.cac.CreativeAdvancementCompletion
import org.imyvm.cac.i18n.Translator

class PlayerListener(
    private val plugin: CreativeAdvancementCompletion
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        Translator.send(player, "player.join", player.name)
    }
}