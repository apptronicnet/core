package net.apptronic.core.component

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.*
import net.apptronic.core.component.entity.extensions.setup
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.timer.Timer
import net.apptronic.core.component.timer.TimerTick
import net.apptronic.core.mvvm.viewmodel.ComponentRegistry
import net.apptronic.core.threading.WorkerDefinition

open class Component(
    private val context: Context
) : Context by context {

    override fun getToken(): Context {
        return context
    }

    open fun getContext(): Context {
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

    fun update(block: () -> Unit) {
        getScheduler().execute(WorkerDefinition.DEFAULT, block)
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

    /**
     * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
     */
    fun now(): Entity<Unit> {
        return EmptyChain(this)
    }

    fun <T> entity(source: Entity<T>): Entity<T> {
        return value<T>().setAs(source)
    }

    fun <T> entity(source: Entity<T>, defaultValue: T): Entity<T> {
        return value<T>(defaultValue).setAs(source)
    }

    fun timer(
        interval: Long,
        worker: WorkerDefinition = WorkerDefinition.DEFAULT,
        limit: Long = Timer.INFINITE,
        action: ((TimerTick) -> Unit)? = null
    ): Timer {
        return Timer(this, initialInterval = interval, worker = worker, initialLimit = limit).also {
            if (action != null) {
                it.observe(action)
            }
        }
    }

}
