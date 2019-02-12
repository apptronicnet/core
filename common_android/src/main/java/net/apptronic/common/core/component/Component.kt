package net.apptronic.common.core.component

import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.entities.*
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStage
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.mvvm.process.InViewBackgroundProcess
import net.apptronic.common.core.mvvm.viewmodel.ComponentRegistry

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
     * LiveModelProperty of view
     */
    fun <T> value(): LiveModelProperty<T> {
        return LiveModelValue(context)
    }

    /**
     * LiveModelProperty of view with some default value
     */
    fun <T> value(defaultValue: T): LiveModelProperty<T> {
        return value<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> mutableValue(): LiveModelMutableValue<T> {
        return LiveModelMutableValue(this)
    }

    fun <T> mutableValue(defaultValue: T): LiveModelMutableValue<T> {
        return mutableValue<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

    fun <K, V> valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

    fun <T> valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

    /**
     * LiveModelProperty of view
     */
    fun <T> function(predicate: Predicate<T>): LiveModelProperty<T> {
        return value<T>().setAs(predicate)
    }

    /**
     * User action on screen
     */
    fun genericEvent(): LiveModelGenericEvent {
        return LiveModelGenericEvent(this)
    }

    /**
     * User action on screen
     */
    fun <T> typedEvent(): ViewModelTypedEvent<T> {
        return ViewModelTypedEvent(this)
    }

    abstract class SubModel(parent: Component) : Component(parent)

    fun <T> resultListener(onReceived: (T) -> Unit): ResultListener<T> {
        val resultEvent = typedEvent<T>()
        resultEvent.subscribe(onReceived)
        return object : ResultListener<T> {
            override fun setResult(result: T) {
                workers().execute(ContextWorkers.DEFAULT) {
                    resultEvent.sendEvent(result)
                }
            }
        }

    }

    fun update(block: () -> Unit) {
        workers().execute(ContextWorkers.DEFAULT, block)
    }

    fun <T, R> backgroundProcess(action: (T) -> R): InViewBackgroundProcess<T, R> {
        return InViewBackgroundProcess(this, action)
    }

}
