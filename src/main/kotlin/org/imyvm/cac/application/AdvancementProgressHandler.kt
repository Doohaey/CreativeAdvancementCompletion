package org.imyvm.cac.application

import org.bukkit.GameMode
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.imyvm.cac.domain.AdvancementItemRegistry
import org.imyvm.cac.domain.event.EventStatus

object AdvancementProgressHandler {

    fun validateAndProcess(player: Player, advancement: Advancement) {
        if (!shouldAllow(player)) {
            resetProgress(player, advancement)
            resetPlayerPhysicalStatus(player, advancement)
        }
    }

    fun performDeepCleanup(player: Player) {
        if (!shouldAllow(player)) {
            val iterator = player.server.advancementIterator()
            while (iterator.hasNext()) {
                val advancement = iterator.next()
                val progress = player.getAdvancementProgress(advancement)

                if (progress.isDone) {
                    resetProgress(player, advancement)
                    resetPlayerPhysicalStatus(player, advancement)
                }
            }
        }
    }

    private fun shouldAllow(player: Player): Boolean {
        return EventStatus.isActive() && player.gameMode == GameMode.CREATIVE
    }

    private fun resetProgress(player: Player, advancement: Advancement) {
        val progress = player.getAdvancementProgress(advancement)
        progress.awardedCriteria.forEach { progress.revokeCriteria(it) }
    }

    private fun resetPlayerPhysicalStatus(player: Player, advancement: Advancement) {
        removeItemIfNecessary(player, advancement)
    }

    private fun removeItemIfNecessary(player: Player, advancement: Advancement) {
        val key = advancement.key.toString()
        val materialsToRemove = AdvancementItemRegistry.getItemsForAdvancement(key) ?: return

        val inventory = player.inventory
        val contents = inventory.contents

        for (i in contents.indices) {
            val item = contents[i] ?: continue

            if (materialsToRemove.contains(item.type)) {
                inventory.setItem(i, null)
            }
        }
    }
}