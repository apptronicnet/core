package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.threading.Worker

interface WorkerSource {

    fun getWorker(): Worker

}