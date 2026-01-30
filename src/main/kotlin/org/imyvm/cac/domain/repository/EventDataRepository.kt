package org.imyvm.cac.domain.repository

import org.imyvm.cac.domain.model.PlayerProgress
import java.util.UUID

object EventRepository {
    private val playerStats = mutableMapOf<UUID, PlayerProgress>()

    fun getOrCreateProgress(uuid: UUID): PlayerProgress =
        playerStats.getOrPut(uuid) { PlayerProgress(uuid) }

    fun getGlobalHistory(advKey: String): List<Pair<UUID, java.time.Instant>> {
        return playerStats.values.mapNotNull { progress ->
            val record = progress.acquired.find { it.key == advKey }
            if (record != null) progress.uuid to record.timestamp else null
        }.sortedBy { it.second }
    }
}