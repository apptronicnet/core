package net.apptronic.common.core.component

import net.apptronic.common.core.component.entity.*
import net.apptronic.common.core.component.entity.functions.Predicate
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStage
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.mvvm.process.InViewBackgroundProcess
import net.apptronic.common.core.mvvm.viewmodel.ComponentRegistry
import net.apptronic.common.core.mvvm.viewmodel.ViewModelParent

open class Component(
    private val context: ComponentContext
) : ComponentContext by context {

    private val id: Long = ComponentRegistry.nextId()
    private var parent: ViewModelParent? = null

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
     * ViewModelProperty of view
     */
    fun <T> value(): ViewModelProperty<T> {
        return ViewModelValue(context)
    }

    /**
     * ViewModelProperty of view with some default value
     */
    fun <T> value(defaultValue: T): ViewModelProperty<T> {
        return value<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> mutableValue(): ViewModelMutableValue<T> {
        return ViewModelMutableValue(this)
    }

    fun <T> mutableValue(defaultValue: T): ViewModelMutableValue<T> {
        return mutableValue<T>().setup {
            set(defaultValue)
        }
    }

    fun <T> valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

    fun <K, V> valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

    fun <T> valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

    /**
     * ViewModelProperty of view
     */
    fun <T> function(predicate: Predicate<T>): ViewModelProperty<T> {
        return value<T>().setAs(predicate)
    }

    /**
     * User action on screen
     */
    fun genericEvent(): ViewModelGenericEvent {
        return ViewModelGenericEvent(this)
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
                workers().execute {
                    resultEvent.sendEvent(result)
                }
            }
        }

    }

    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            workers().execute {
                it.requestCloseSelf(this, transitionInfo)
            }
            true
        } ?: false
    }

    fun onAttachToParent(parent: ViewModelParent) {
        this.parent = parent
    }

    fun onDetachFromParent() {
        this.parent = null
    }

    fun update(block: () -> Unit) {
        workers().execute(block)
    }

    fun <T, R> backgroundProcess(action: (T) -> R): InViewBackgroundProcess<T, R> {
        return InViewBackgroundProcess(this, action)
    }

}
