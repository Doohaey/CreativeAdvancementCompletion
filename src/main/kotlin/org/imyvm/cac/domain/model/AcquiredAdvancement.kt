package org.imyvm.cac.domain.model

import java.time.Instant

data class AcquiredAdvancement(
    val key: String,
    val category: String,
    val weight: Int,
    val timestamp: String = Instant.now().toString(),
    val sessionId: String? = null,
)
