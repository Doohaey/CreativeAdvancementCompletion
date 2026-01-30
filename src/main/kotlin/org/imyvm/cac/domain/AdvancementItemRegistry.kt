package org.imyvm.cac.domain

import org.bukkit.Material

object AdvancementItemRegistry {

    fun getItemsForAdvancement(key: String): List<Material>? {
        return when {
            key.startsWith("minecraft:story/") -> getStoryItems(key)
            key.startsWith("minecraft:nether/") -> getNetherItems(key)
            key.startsWith("minecraft:end/") -> getEndItems(key)
            key.startsWith("minecraft:adventure/") -> getAdventureItems(key)
            key.startsWith("minecraft:husbandry/") -> getHusbandryItems(key)
            else -> null
        }
    }

    private fun getStoryItems(key: String): List<Material>? {
        return when (key) {
            "minecraft:story/root" -> listOf(Material.CRAFTING_TABLE)
            "minecraft:story/mine_stone" -> listOf(Material.COBBLESTONE, Material.BLACKSTONE, Material.COBBLED_DEEPSLATE)
            "minecraft:story/upgrade_tools" -> listOf(Material.STONE_PICKAXE)
            "minecraft:story/smelt_iron" -> listOf(Material.IRON_INGOT)
            "minecraft:story/obtain_armor" -> listOf(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)
            "minecraft:story/lava_bucket" -> listOf(Material.LAVA_BUCKET)
            "minecraft:story/iron_tools" -> listOf(Material.IRON_PICKAXE)
            "minecraft:story/form_obsidian" -> listOf(Material.OBSIDIAN)
            "minecraft:story/mine_diamond" -> listOf(Material.DIAMOND)
            "minecraft:story/shiny_gear" -> listOf(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)
            else -> null
        }
    }

    private fun getNetherItems(key: String): List<Material>? {
        return when (key) {
            "minecraft:nether/obtain_ancient_debris" -> listOf(Material.ANCIENT_DEBRIS)
            "minecraft:nether/obtain_crying_obsidian" -> listOf(Material.CRYING_OBSIDIAN)
            "minecraft:nether/ride_strider" -> listOf(Material.WARPED_FUNGUS_ON_A_STICK)
            "minecraft:nether/netherite_armor" -> listOf(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS)
            "minecraft:nether/get_wither_skull" -> listOf(Material.WITHER_SKELETON_SKULL)
            "minecraft:nether/obtain_blaze_rod" -> listOf(Material.BLAZE_ROD)
            "minecraft:nether/brew_potion" -> listOf(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION)
            else -> null
        }
    }

    private fun getEndItems(key: String): List<Material>? {
        return when (key) {
            "minecraft:end/respawn_dragon" -> listOf(Material.END_CRYSTAL)
            "minecraft:end/dragon_egg" -> listOf(Material.DRAGON_EGG)
            "minecraft:end/dragon_breath" -> listOf(Material.DRAGON_BREATH)
            "minecraft:end/elytra" -> listOf(Material.ELYTRA)
            else -> null
        }
    }

    private fun getAdventureItems(key: String): List<Material>? {
        return when (key) {
            "minecraft:adventure/spyglass_at_parrot",
            "minecraft:adventure/spyglass_at_ghast",
            "minecraft:adventure/spyglass_at_dragon" -> listOf(Material.SPYGLASS)
            else -> null
        }
    }

    private fun getHusbandryItems(key: String): List<Material>? {
        return when (key) {
            "minecraft:husbandry/obtain_sniffer_egg" -> listOf(Material.SNIFFER_EGG)
            else -> null
        }
    }
}