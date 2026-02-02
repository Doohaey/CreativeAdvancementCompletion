package org.imyvm.cac.domain.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.imyvm.cac.domain.event.EventStatus
import org.imyvm.cac.domain.model.AcquiredAdvancement
import org.imyvm.cac.domain.model.PlayerProgress
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.UUID

object EventRepository {
    private val playerStats = mutableMapOf<UUID, PlayerProgress>()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun getOrCreateProgress(uuid: UUID): PlayerProgress =
        playerStats.getOrPut(uuid) {
            PlayerProgress(uuid)
        }

    fun getAllScores(): List<Pair<String, Int>> {
        return playerStats.mapNotNull { (uuid, progress) ->
            val score = progress.getScoreValid()
            if (score > 0 || EventStatus.isActive()) {
                val name = Bukkit.getPlayer(uuid)?.name ?: Bukkit.getOfflinePlayer(uuid).name ?: "Unknown"
                name to score
            } else null
        }
    }

    fun save(plugin: JavaPlugin) {
        val file = File(plugin.dataFolder, "data.json")
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

        val exportData = playerStats.mapKeys { it.key.toString() }.mapValues { it.value.acquiredValid }

        try {
            FileWriter(file).use { writer ->
                gson.toJson(exportData, writer)
            }
        } catch (e: Exception) {
            plugin.logger.severe("Could not save data.json: ${e.message}")
        }
    }

    fun load(plugin: JavaPlugin) {
        val file = File(plugin.dataFolder, "data.json")
        if (!file.exists()) return

        try {
            FileReader(file).use { reader ->
                val type = object : TypeToken<Map<String, List<AcquiredAdvancement>>>() {}.type
                val importedData: Map<String, List<AcquiredAdvancement>> = gson.fromJson(reader, type) ?: return

                importedData.forEach { (uuidStr, history) ->
                    val uuid = UUID.fromString(uuidStr)
                    getOrCreateProgress(uuid).loadHistory(history)
                }
            }
        } catch (e: Exception) {
            plugin.logger.severe("Could not load data.json: ${e.message}")
        }
    }
}