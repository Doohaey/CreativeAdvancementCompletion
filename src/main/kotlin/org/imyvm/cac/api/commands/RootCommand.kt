package org.imyvm.cac.api.commands

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.imyvm.cac.event.EventStatus
import org.imyvm.cac.i18n.Translator

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
            .executes(::execStatus)
            .build()

        registrar.register(root)
    }

    private fun execStatus(ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>): Int {
        sendStatus(ctx.source.sender)
        return Command.SINGLE_SUCCESS
    }

    private fun execStart(ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        EventStatus.setActive(true, sender.name)
        Translator.send(sender, "command.changed.started")
        return Command.SINGLE_SUCCESS
    }

    private fun execStop(ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        EventStatus.setActive(false, sender.name)
        Translator.send(sender, "command.changed.stopped")
        return Command.SINGLE_SUCCESS
    }

    private fun execToggle(ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>): Int {
        val sender = ctx.source.sender
        val newStatus = !EventStatus.isActive()
        EventStatus.setActive(newStatus, sender.name)
        val key = if (newStatus) "command.changed.started" else "command.changed.stopped"
        Translator.send(sender, key)
        return Command.SINGLE_SUCCESS
    }

    private fun sendStatus(sender: org.bukkit.command.CommandSender) {
        val key = if (EventStatus.isActive()) "command.status.active" else "command.status.inactive"
        Translator.send(sender, key)
    }
}