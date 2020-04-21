package net.apptronic.core.threading

import net.apptronic.core.component.context.Context

@Deprecated("Should use coroutines")
interface WorkerProvider {

    fun provideWorker(context: Context): Worker

}

@Deprecated("Should use coroutines")
class FactoryWorkerProvider(
        private val builder: () -> Worker
) : WorkerProvider {

    override fun provideWorker(context: Context): Worker {
        return builder.invoke()
    }

}

@Deprecated("Should use coroutines")
class InstanceWorkerProvider(
        private val worker: Worker
) : WorkerProvider {

    override fun provideWorker(context: Context): Worker {
        return worker
    }

}