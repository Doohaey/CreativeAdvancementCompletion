package org.imyvm.cac.application

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.imyvm.cac.domain.event.EventStatus

class AdvancementProgressHandler {

    fun validateAndProcess(player: Player, advancement: Advancement) {
        if (!shouldAllow(player)) {
            resetProgress(player, advancement)
            removeItemIfNecessary(player, advancement)
        }
    }

    private fun shouldAllow(player: Player): Boolean {
        return EventStatus.isActive() && player.gameMode == GameMode.CREATIVE
    }

    private fun resetProgress(player: Player, advancement: Advancement) {
        val progress = player.getAdvancementProgress(advancement)
        progress.awardedCriteria.forEach { progress.revokeCriteria(it) }
    }

    private fun removeItemIfNecessary(player: Player, advancement: Advancement) {
        val key = advancement.key.toString()
        val materialsToRemove = getItemsForAdvancement(key) ?: return

        val inventory = player.inventory
        val contents = inventory.contents

        for (i in contents.indices) {
            val item = contents[i] ?: continue

            if (materialsToRemove.contains(item.type)) {
                inventory.setItem(i, null)
            }
        }
    }

    private fun getItemsForAdvancement(key: String): List<Material>? {
        return when (key) {
            "minecraft:story/root" -> listOf(Material.CRAFTING_TABLE)
            "minecraft:story/mine_stone" -> listOf(Material.COBBLESTONE, Material.BLACKSTONE, Material.COBBLED_DEEPSLATE)
            "minecraft:story/upgrade_tool" -> listOf(Material.STONE_PICKAXE)
            "minecraft:story/smelt_iron" -> listOf(Material.IRON_INGOT)
            "minecraft:story/iron_tools" -> listOf(Material.IRON_PICKAXE)
            "minecraft:nether/get_wither_skull" -> listOf(Material.WITHER_SKELETON_SKULL)
            else -> null
        }
    }
}