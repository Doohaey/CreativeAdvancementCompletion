package org.imyvm.cac.entrypoints.listeners

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.imyvm.cac.application.AdvancementProgressHandler
import org.imyvm.cac.CreativeAdvancementCompletion
import org.imyvm.cac.util.i18n.Translator

class PlayerListener(
    private val plugin: CreativeAdvancementCompletion
) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        Translator.send(player, "player.join", player.name)
    }

    @EventHandler
    fun onAdvancementDone(event: PlayerAdvancementDoneEvent) {
        val player = event.player
        val advancement = event.advancement

        AdvancementProgressHandler().validateAndProcess(player, advancement)
    }
}