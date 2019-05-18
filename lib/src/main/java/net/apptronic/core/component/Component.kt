package net.apptronic.core.component

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.*
import net.apptronic.core.component.entity.extensions.setup
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.process.BackgroundAction
import net.apptronic.core.component.process.BackgroundProcess
import net.apptronic.core.component.process.setup
import net.apptronic.core.mvvm.viewmodel.ComponentRegistry
import net.apptronic.core.threading.WorkerDefinition

open class Component(
    val context: Context
) : Context by context {

    override fun getToken(): Context {
        return context
    }

    private val id: Long = ComponentRegistry.nextId()

    open fun getDefaultWorker(): WorkerDefinition = WorkerDefinition.SYNCHRONOUS

    fun getId(): Long = id

    fun onceStage(stageName: String, key: String, action: () -> Unit) {
        getLifecycle().getStage(stageName)?.doOnce(key, action)
    }

    fun onEnterStage(stageName: String, callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        getLifecycle().getStage(stageName)?.doOnEnter(callback)
    }

    fun onExitStage(stageName: String, callback: LifecycleStage.OnExitHandler.() -> Unit) {
        getLifecycle().getStage(stageName)?.doOnExit(callback)
    }

    fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(Lifecycle.ROOT_STAGE, callback)
    }

    override fun getLifecycle(): Lifecycle {
        return context.getLifecycle()
    }

    /**
     * Property of view
     */
    fun <T> value(): Property<T> {
        return Value(context)
    }

    /**
     * Property of view with some default value
     */
    fun <T> value(defaultValue: T): Property<T> {
        return value<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> mutableValue(): MutableValue<T> {
        return MutableValue(this)
    }

    fun <T> mutableValue(defaultValue: T): MutableValue<T> {
        return mutableValue<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

    fun <K, V> valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

    fun <T> valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

    /**
     * User action on screen
     */
    fun genericEvent(): GenericEvent {
        return GenericEvent(context)
    }

    fun genericEvent(observer: Observer<Unit>): GenericEvent {
        return GenericEvent(context).apply {
            subscribe(observer)
        }
    }

    fun genericEvent(callback: () -> Unit): GenericEvent {
        return GenericEvent(this).apply {
            subscribe {
                callback.invoke()
            }
        }
    }

    /**
     * User action on screen
     */
    fun <T> typedEvent(): Event<T> {
        return TypedEvent(context)
    }

    fun <T> typedEvent(observer: Observer<T>): Event<T> {
        return TypedEvent<T>(context).apply {
            subscribe(observer)
        }
    }

    fun <T> typedEvent(callback: (T) -> Unit): Event<T> {
        return TypedEvent<T>(context).apply {
            subscribe(callback)
        }
    }

    fun <T> toggle(target: Property<T>, vararg values: T): Toggle<T> {
        return Toggle(target, *values)
    }

    fun toggle(target: Property<Boolean>): Toggle<Boolean> {
        return Toggle(target, false, true)
    }

    abstract class SubModel(parent: Component) : Component(parent)

    fun update(block: () -> Unit) {
        getScheduler().execute(WorkerDefinition.DEFAULT, block)
    }

    fun <T, R> backgroundProcess(
        workerDefinition: WorkerDefinition,
        action: BackgroundAction<T, R>,
        setupBlock: BackgroundProcess<T, R>.() -> Unit = {}
    ): BackgroundProcess<T, R> {
        return BackgroundProcess(this, action, workerDefinition).setup(setupBlock)
    }

    fun <T, R> backgroundProcess(
        action: BackgroundAction<T, R>,
        setupBlock: BackgroundProcess<T, R>.() -> Unit = {}
    ): BackgroundProcess<T, R> {
        return BackgroundProcess(this, action).setup(setupBlock)
    }

    open fun terminate() {
        getLifecycle().terminate()
    }

    /**
     * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
     */
    fun newChain(): Entity<Unit> {
        return EmptyChain(this)
    }

}
