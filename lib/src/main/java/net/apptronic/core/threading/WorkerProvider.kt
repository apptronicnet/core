package net.apptronic.core.threading

interface WorkerProvider {

    fun provideWorker(): Worker

}

class FactoryWorkerProvider(
    private val builder: () -> Worker
) : WorkerProvider {

    override fun provideWorker(): Worker {
        return builder.invoke()
    }

}

class InstanceWorkerProvider(
    private val worker: Worker
) : WorkerProvider {

    override fun provideWorker(): Worker {
        return worker
    }

}