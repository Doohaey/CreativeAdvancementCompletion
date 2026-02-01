package org.imyvm.cac.entrypoint.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.imyvm.cac.domain.event.EventStatus
import org.imyvm.cac.domain.repository.EventRepository
import org.imyvm.cac.util.i18n.Translator

object RootCommand {
    fun register(registrar: Commands) {
        val root = Commands.literal("cac")
            .then(
                Commands.literal("status")
                    .executes(::execStatus)
            )
            .then(
                Commands.literal("start")
                    .requires { it.sender.isOp || it.sender.hasPermission("cac.admin") }
                    .executes(::execStart)
            )
            .then(
                Commands.literal("stop")
                    .requires { it.sender.isOp || it.sender.hasPermission("cac.admin") }
                    .executes(::execStop)
            )
            .then(
                Commands.literal("toggle")
                    .requires { it.sender.isOp || it.sender.hasPermission("cac.admin") }
                    .executes(::execToggle)
            )
            .then(
                Commands.literal("me")
                    .executes(::execMe)
            )
            .then(
                Commands.literal("top")
                    .executes(::execTop)
            )
            .then(
                Commands.literal("check")
                    .then(
                        Commands.argument("player", StringArgumentType.string())
                            .executes(::execCheck)
                    )
            )
            .then(
                Commands.literal("help")
                    .executes(::execHelp)
            )
            .executes(::execStatus)
            .build()

        registrar.register(root)
    }

    private fun execStatus(ctx: CommandContext<CommandSourceStack>): Int {
        sendStatus(ctx.source.sender)
        return Command.SINGLE_SUCCESS
    }

    private fun execStart(ctx:CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        EventStatus.setActive(true, sender.name)
        Translator.send(sender, "command.changed.started")
        return Command.SINGLE_SUCCESS
    }

    private fun execStop(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        EventStatus.setActive(false, sender.name)
        Translator.send(sender, "command.changed.stopped")
        return Command.SINGLE_SUCCESS
    }

    private fun execToggle(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val newStatus = !EventStatus.isActive()
        EventStatus.setActive(newStatus, sender.name)
        val key = if (newStatus) "command.changed.started" else "command.changed.stopped"
        Translator.send(sender, key)
        return Command.SINGLE_SUCCESS
    }

    private fun execMe(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val player = sender as? Player ?: return Command.SINGLE_SUCCESS
        val progress = EventRepository.getOrCreateProgress(player.uniqueId)
        val allScores = EventRepository.getAllScores()

        val categories = listOf("story", "nether", "end", "adventure", "husbandry")
        val tableRows = StringBuilder()
        val legacy = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()

        var totalA = 0
        var totalG = 0
        var totalC = 0

        categories.forEach { cat ->
            val a = progress.acquired.count { it.category == cat && it.weight == 1 }
            val g = progress.acquired.count { it.category == cat && it.weight == 3 }
            val c = progress.acquired.count { it.category == cat && it.weight == 5 }
            val score = progress.getScore(cat)

            totalA += a
            totalG += g
            totalC += c

            val rowComp = Translator.tr("command.me.row",
                cat.replaceFirstChar { it.uppercase() }.padEnd(10), a, g, c, score)

            if (rowComp != null) {
                tableRows.append(legacy.serialize(rowComp)).append("\n")
            }
        }

        val totalRowComp = Translator.tr("command.me.total_row",
            "Total".padEnd(10), totalA, totalG, totalC, progress.totalScore)

        if (totalRowComp != null) {
            tableRows.append(legacy.serialize(totalRowComp)).append("\n")
        }

        Translator.send(sender, "command.me.report",
            player.name,
            progress.getRank(allScores),
            tableRows.toString(),
            progress.totalScore
        )
        return Command.SINGLE_SUCCESS
    }

    private fun execTop(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val scores = EventRepository.getAllScores().sortedByDescending { it.second }

        val leaderboard = scores.mapIndexed { index, entry ->
            "&e#${index + 1} &f${entry.first}: &b${entry.second}"
        }.joinToString("\n")

        Translator.send(sender, "command.top", leaderboard)
        return Command.SINGLE_SUCCESS
    }

    private fun execCheck(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val targetName = ctx.getArgument("player", String::class.java)
        val target = Bukkit.getPlayer(targetName) ?: return Command.SINGLE_SUCCESS

        val progress = EventRepository.getOrCreateProgress(target.uniqueId)

        val challengeKeys = progress.getChallenges().joinToString("\n&7 - &d") {
            it.replace("minecraft:", "").substringAfter("/")
        }.ifEmpty { "None" }

        Translator.send(sender, "command.check.report", target.name, progress.totalScore, challengeKeys)
        return Command.SINGLE_SUCCESS
    }

    private fun execHelp(ctx: CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        Translator.send(sender, "command.help", "Advancement: 1", "Goal: 3", "Challenge: 5")
        return Command.SINGLE_SUCCESS
    }


    private fun sendStatus(sender: org.bukkit.command.CommandSender) {
        val key = if (EventStatus.isActive()) "command.status.active" else "command.status.inactive"
        Translator.send(sender, key)
    }
}