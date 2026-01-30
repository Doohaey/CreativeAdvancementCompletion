package org.imyvm.cac.entrypoint.commands

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.imyvm.cac.CreativeAdvancementCompletion

class CommandManager(
    private val plugin: CreativeAdvancementCompletion
){
    fun setup(){
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val registrar = event.registrar()
            RootCommand.register(registrar)
        }
    }
}