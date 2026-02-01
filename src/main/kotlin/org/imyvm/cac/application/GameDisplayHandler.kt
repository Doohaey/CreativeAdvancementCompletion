package org.imyvm.cac.application

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.RenderType
import org.imyvm.cac.domain.repository.EventRepository
import org.imyvm.cac.util.i18n.Translator
import kotlin.to

object GameDisplayHandler {
    private val legacySerializer = LegacyComponentSerializer.legacySection()
    private val bossBars = mutableMapOf<Player, BossBar>()

    fun updateScoreboards(plugin: JavaPlugin) {
        val allScores = EventRepository.getAllScores().sortedByDescending { it.second }
        val topN = plugin.config.getInt("scoreboard.player.amount", 5)

        Bukkit.getOnlinePlayers().forEach { player ->
            val manager = Bukkit.getScoreboardManager()
            val scoreboard = manager.newScoreboard

            val title = Translator.tr("scoreboard.header") ?: Component.text("TOP COMPETITORS")
            val objective = scoreboard.registerNewObjective(
                "game_race",
                Criteria.DUMMY,
                title,
                RenderType.INTEGER
            )

            objective.displaySlot = DisplaySlot.SIDEBAR

            val topEntries = allScores.take(topN)
            var lineWeight = 15

            for (i in 0 until topN) {
                val component = if (i < topEntries.size) {
                    val (name, score) = topEntries[i]
                    Translator.tr("scoreboard.top.entry", name, score)
                } else {
                    Translator.tr("scoreboard.top.empty")
                } ?: Component.text("---")

                val entryString = legacySerializer.serialize(component)
                objective.getScore(entryString).score = lineWeight--
            }

            objective.getScore("§r ").score = lineWeight--

            val progress = EventRepository.getOrCreateProgress(player.uniqueId)
            val rankIndex = allScores.indexOfFirst { it.first == player.name }
            val rank = if (rankIndex == -1) 1 else rankIndex + 1

            val statsComp = Translator.tr("scoreboard.player", progress.totalScore, rank)
                ?: Component.text("Score: ${progress.totalScore}")

            objective.getScore(legacySerializer.serialize(statsComp)).score = lineWeight--

            objective.getScore("§7 ").score = lineWeight-- // Another unique spacer
            val footerComp = Translator.tr("score.item") ?: Component.text("A:1 | G:3 | C:5")
            objective.getScore(legacySerializer.serialize(footerComp)).score = lineWeight--

            player.scoreboard = scoreboard
        }
    }

    fun updateBossBars() {
        val players = Bukkit.getOnlinePlayers()
        val scores = players.map { it to EventRepository.getOrCreateProgress(it.uniqueId).getScore() }
            .sortedByDescending { it.second }

        val leader = scores.firstOrNull() ?: return
        val leaderScore = leader.second

        players.forEach { player ->
            val playerScore = scores.find { it.first == player }?.second ?: 0
            val progress = playerScore.toDouble() / leaderScore
            val color = when {
                progress >= 0.9 -> BossBar.Color.GREEN
                progress >= 0.7 -> BossBar.Color.YELLOW
                else -> BossBar.Color.RED
            }

            val bossBar = this.bossBars.computeIfAbsent(player) {
                BossBar.bossBar(Component.text(""), 0f, color, BossBar.Overlay.PROGRESS)
            }

            bossBar.name(
                Translator.tr("boss_bar.leader", leader.first.name, leaderScore, leaderScore - playerScore)
                    ?: Component.text("Leader: ${leader.first.name} - ${leaderScore}pts")
            )
            bossBar.progress(progress.toFloat())
            bossBar.color(color)

            player.showBossBar(bossBar)
        }
    }

    fun showActionBar(player: Player, advancementName: String, points: Int, category: String, total: Int) {
        val message = Translator.tr("actionbar.advancement", advancementName, points, category, total)
        player.sendActionBar(message ?: Component.text("Advancement: $advancementName (+$points) | $category: $total"))
    }

    fun sendChatMessage(player: Player, advancementName: String, points: Int, isChallenge: Boolean) {
        val messageKey = if (isChallenge) "chat.challenge" else "chat.advancement"
        val message = Translator.tr(messageKey, player.name, advancementName, points)
        Bukkit.getOnlinePlayers().forEach { it.sendMessage(message ?: Component.text("${player.name} earned $advancementName (+$points)")) }
    }
}