package org.imyvm.cac.domain.model

import org.bukkit.Bukkit
import org.bukkit.advancement.Advancement
import java.time.Instant
import java.util.*

data class AcquiredAdvancement(
    val key: String,
    val category: String,
    val weight: Int,
    val timestamp: String = Instant.now().toString()
)

class PlayerProgress(val uuid: UUID) {
    private val _acquired = mutableListOf<AcquiredAdvancement>()
    val acquired: List<AcquiredAdvancement> get() = _acquired

    val totalScore: Int
        get() = _acquired.sumOf { it.weight }

    fun loadHistory(history: List<AcquiredAdvancement>) {
        _acquired.clear()
        _acquired.addAll(history)
    }

    fun add(advancement: Advancement, category: String, weight: Int) {
        if (_acquired.none { it.key == advancement.key.toString() }) {
            _acquired.add(AcquiredAdvancement(advancement.key.toString(), category, weight))
        }
    }

    fun getScore(category: String? = null): Int {
        return if (category == null) {
            totalScore
        } else {
            _acquired
                .filter { it.category.equals(category, ignoreCase = true) }
                .sumOf { it.weight }
        }
    }

    fun getRank(allScores: List<Pair<String, Int>>): Int {
        val sortedScores = allScores.sortedByDescending { it.second }
        return sortedScores.indexOfFirst { it.first == Bukkit.getOfflinePlayer(uuid).name } + 1
    }

    fun getCategoryDetails(): Map<String, Int> {
        return _acquired.groupBy { it.category }
            .mapValues { (_, advancements) -> advancements.sumOf { it.weight } }
    }

    fun getChallenges(): List<String> {
        return _acquired.filter { it.weight == 5 }.map { it.key }
    }
}
