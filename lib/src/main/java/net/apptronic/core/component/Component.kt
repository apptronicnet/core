package net.apptronic.core.component

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.entities.*
import net.apptronic.core.component.entity.setup
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.mvvm.process.InViewBackgroundProcess
import net.apptronic.core.mvvm.viewmodel.ComponentRegistry

open class Component(
    val context: ComponentContext
) : ComponentContext by context {

    private val id: Long = ComponentRegistry.nextId()

    open fun getDefaultWorker() = ContextWorkers.SYNCHRONOUS

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
     * Property of view
     */
    fun <T> function(predicate: Predicate<T>): Property<T> {
        return value<T>().setup { setAs(predicate) }
    }

    /**
     * User action on screen
     */
    fun genericEvent(): ComponentGenericEvent {
        return ComponentGenericEvent(this)
    }

    /**
     * User action on screen
     */
    fun <T> typedEvent(): ComponentEvent<T> {
        return ComponentTypedEvent(context)
    }

    abstract class SubModel(parent: Component) : Component(parent)

    fun update(block: () -> Unit) {
        workers().execute(ContextWorkers.DEFAULT, block)
    }

    fun <T, R> backgroundProcess(action: (T) -> R): InViewBackgroundProcess<T, R> {
        return InViewBackgroundProcess(this, action)
    }

}
