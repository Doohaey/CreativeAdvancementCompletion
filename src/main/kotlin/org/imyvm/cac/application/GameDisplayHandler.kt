package org.imyvm.cac.application

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.imyvm.cac.domain.repository.EventRepository
import org.imyvm.cac.util.i18n.Translator
import kotlin.to

object GameDisplayHandler {
    private val bossBars = mutableMapOf<Player, BossBar>()

    fun updateScoreboards() {
        val players = Bukkit.getOnlinePlayers()
        val scores = players.map { it to EventRepository.getOrCreateProgress(it.uniqueId).getScore() }
            .sortedByDescending { it.second }

        players.forEach { player ->
            val playerScore = scores.find { it.first == player }?.second ?: 0
            val rank = scores.indexOfFirst { it.first == player } + 1
            val top5 = scores.take(5)

            val top5Text = top5.joinToString("\n") { (p, score) ->
                Translator.tr("scoreboard.top5.entry", p.name, score)?.toString() ?: "${p.name}: $score"
            }

            val scoreboard = Translator.tr("scoreboard.header")?.append(Component.text("\n"))
                ?.append(Component.text(top5Text))
                ?.append(Component.text("\n"))
                ?.append(Translator.tr("scoreboard.player", playerScore, rank)!!)
                ?.append(Component.text("\n"))
                ?.append(Translator.tr("scoreboard.footer")!!)
                ?: Component.text("Top 5 Players:\n$top5Text\nYour Score: $playerScore (Rank: $rank)\nScore Formula: k=1, 3, 5")

            player.sendPlayerListHeader(scoreboard)
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
                Translator.tr("bossbar.leader", leader.first.name, leaderScore, leaderScore - playerScore)
                    ?: Component.text("Leader: ${leader.first.name} - ${leaderScore}pts")
            )
            bossBar.progress(progress.toFloat())
            bossBar.color(color)

            player.showBossBar(bossBar)
        }
    }
}