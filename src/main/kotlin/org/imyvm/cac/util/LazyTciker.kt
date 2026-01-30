package org.imyvm.cac.util

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object LazyTicker {
    private val tasks = mutableListOf<() -> Unit>()
    private var bukkitTask: org.bukkit.scheduler.BukkitTask? = null

    fun registerTask(task: () -> Unit) {
        tasks.add(task)
    }

    fun start(plugin: JavaPlugin, seconds: Long) {
        if (bukkitTask != null) return

        val ticks = seconds * 20L
        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            tasks.forEach {
                runCatching { it() }.onFailure { it.printStackTrace() }
            }
        }, ticks, ticks)
    }

    fun stop() {
        bukkitTask?.cancel()
        bukkitTask = null
    }
}