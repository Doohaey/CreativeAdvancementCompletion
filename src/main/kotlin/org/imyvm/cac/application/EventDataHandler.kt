package org.imyvm.cac.application

import io.papermc.paper.advancement.AdvancementDisplay
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.imyvm.cac.domain.repository.EventRepository

object EventDataHandler {

    fun recordAdvancement(player: Player, advancement: Advancement) {
        val display = advancement.display ?: return
        val key = advancement.key.toString()

        val category = key.substringAfter(":").substringBefore("/")

        val weight = when (display.frame()) {
            AdvancementDisplay.Frame.TASK -> 1
            AdvancementDisplay.Frame.GOAL -> 3
            AdvancementDisplay.Frame.CHALLENGE -> 5
        }

        val progress = EventRepository.getOrCreateProgress(player.uniqueId)
        progress.add(advancement, category, weight)
    }
}