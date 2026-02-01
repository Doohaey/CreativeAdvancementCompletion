package org.imyvm.cac.domain.repository

import org.bukkit.Bukkit
import org.imyvm.cac.domain.model.PlayerProgress
import java.util.UUID

object EventRepository {
    private val playerStats = mutableMapOf<UUID, PlayerProgress>()

    fun getOrCreateProgress(uuid: UUID): PlayerProgress =
        playerStats.getOrPut(uuid) { PlayerProgress(uuid) }

    fun getAllScores(): List<Pair<String, Int>> {
        return playerStats.mapNotNull { (uuid, progress) ->
            val name = Bukkit.getOfflinePlayer(uuid).name
            if (name != null) name to progress.totalScore else null
        }
    }
}