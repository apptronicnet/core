package net.apptronic.core.threading

import net.apptronic.core.component.context.Context

interface WorkerProvider {

    fun provideWorker(context: Context): Worker

}

class FactoryWorkerProvider(
        private val builder: () -> Worker
) : WorkerProvider {

    override fun provideWorker(context: Context): Worker {
        return builder.invoke()
    }

}

class InstanceWorkerProvider(
        private val worker: Worker
) : WorkerProvider {

    override fun provideWorker(context: Context): Worker {
        return worker
    }

}