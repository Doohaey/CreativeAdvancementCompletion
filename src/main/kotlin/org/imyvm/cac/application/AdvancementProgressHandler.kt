package org.imyvm.cac.application

import org.bukkit.GameMode
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.imyvm.cac.domain.event.EventStatus

class AdvancementProgressHandler {

    fun validateAndProcess(player: Player, advancement: Advancement) {
        if (!shouldAllow(player)) {
            resetProgress(player, advancement)
        }
    }

    private fun shouldAllow(player: Player): Boolean {
        return EventStatus.isActive() && player.gameMode == GameMode.CREATIVE
    }

    private fun resetProgress(player: Player, advancement: Advancement) {
        val progress = player.getAdvancementProgress(advancement)
        progress.awardedCriteria.forEach { progress.revokeCriteria(it) }
    }
}