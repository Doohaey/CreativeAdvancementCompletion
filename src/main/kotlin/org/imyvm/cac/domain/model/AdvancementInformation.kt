package org.imyvm.cac.domain.model

import org.bukkit.advancement.Advancement
import java.time.Instant
import java.util.*

data class AcquiredAdvancement(
    val key: String,
    val category: String,
    val weight: Int,
    val timestamp: Instant = Instant.now()
)

class PlayerProgress(val uuid: UUID) {
    private val _acquired = mutableListOf<AcquiredAdvancement>()
    val acquired: List<AcquiredAdvancement> get() = _acquired

    val totalScore: Int
        get() = _acquired.sumOf { it.weight }

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
}