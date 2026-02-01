package org.imyvm.cac.entrypoint.tasks

import org.imyvm.cac.application.GameDisplayHandler
import org.imyvm.cac.util.LazyTicker

class GameDisplayTask {

    fun register() {
        LazyTicker.registerTask { GameDisplayHandler.updateScoreboards() }
        LazyTicker.registerTask { GameDisplayHandler.updateBossBars() }
    }
}