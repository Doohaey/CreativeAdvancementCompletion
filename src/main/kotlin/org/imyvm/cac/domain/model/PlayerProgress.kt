package org.imyvm.cac.domain.model

import org.bukkit.Bukkit
import org.bukkit.advancement.Advancement
import org.imyvm.cac.domain.event.EventStatus
import java.time.Instant
import java.util.UUID

class PlayerProgress(
    val uuid: UUID
) {
    private val acquiredTotal = mutableListOf<AcquiredAdvancement>()
    val acquiredValid: List<AcquiredAdvancement>
        get() = acquiredTotal.filter {
            it.sessionId == EventStatus.currentSessionId
        }

    val totalScoreValid: Int
        get() = acquiredValid.sumOf { it.weight }

    fun loadHistory(history: List<AcquiredAdvancement>) {
        acquiredTotal.clear()
        acquiredTotal.addAll(history)
    }

    fun add(
        advancement: Advancement,
        category: String,
        weight: Int
    ) {
        if (acquiredTotal.none {
                it.key == advancement.key.toString()
                        && it.sessionId == EventStatus.currentSessionId
            }) {
            acquiredTotal.add(
                AcquiredAdvancement(
                    advancement.key.toString(),
                    category,
                    weight,
                    Instant.now().toString(),
                    EventStatus.currentSessionId,
                )
            )
        }
    }

    fun getScoreValid(category: String? = null): Int {
        return if (category == null) {
            totalScoreValid
        } else {
            acquiredValid
                .filter { it.category.equals(category, ignoreCase = true) }
                .sumOf { it.weight }
        }
    }

    fun getCountByType(weight: Int): Int = acquiredTotal.count { it.weight == weight }

    fun getCountByCategory(category: String): Int =
        acquiredTotal.count { it.category.equals(category, ignoreCase = true) }

    fun getChallengesList(): List<String> =
        acquiredTotal.filter { it.weight == 5 }.map { it.key }

    fun getRank(allScores: List<Pair<String, Int>>): Int {
        val sortedScores = allScores.sortedByDescending { it.second }
        return sortedScores.indexOfFirst { it.first == Bukkit.getOfflinePlayer(uuid).name } + 1
    }

    fun getCategoryDetails(): Map<String, Int> {
        return acquiredTotal.groupBy { it.category }
            .mapValues { (_, advancements) -> advancements.sumOf { it.weight } }
    }

    fun getChallenges(): List<String> {
        return acquiredTotal.filter { it.weight == 5 }.map { it.key }
    }
}