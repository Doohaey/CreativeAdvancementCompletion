package org.imyvm.cac.entrypoints.listeners

import org.bukkit.GameMode
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.imyvm.cac.CreativeAdvancementCompletion
import org.imyvm.cac.domain.event.EventStatus
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

        if (!shouldAllowAdvancement(player)) {
            revokeAdvancement(player, advancement)
        }
    }

    private fun shouldAllowAdvancement(player: Player): Boolean {
        if (!EventStatus.isActive()) return false
        if (player.gameMode != GameMode.CREATIVE) return false
        return true
    }

    private fun revokeAdvancement(player: Player, advancement: Advancement) {
        val progress = player.getAdvancementProgress(advancement)
        if (progress.isDone) {
            for (criterion in progress.awardedCriteria) {
                progress.revokeCriteria(criterion)
            }
        }
    }
}