package net.apptronic.common.android.ui.viewmodel

import net.apptronic.common.android.ui.viewmodel.adapter.ViewModelStack
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelGenericEvent
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelTypedEvent
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage
import java.util.*

abstract class ViewModel(private val lifecycle: Lifecycle) : LifecycleHolder {

    private val id: Long = ViewModelRegistry.nextId()
    private val innerStacks = LinkedList<ViewModelStack>()

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

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    fun startLifecycle() {
        lifecycle.start()
        getLifecycle().getStage(Lifecycle.ROOT_STAGE)!!.doOnExit {
            ViewModelRegistry.add(this@ViewModel)
            onStartLifecycle()
        }
        getLifecycle().getStage(Lifecycle.ROOT_STAGE)!!.doOnExit {
            onTerminateLifecycle()
            lifecycle.finish()
            ViewModelRegistry.remove(this@ViewModel)
        }
    }

    open fun onStartLifecycle() {
    }

    open fun onTerminateLifecycle() {
    }

    /**
     * Called to end lifecycle for this view model: lifecycle will be forced to be exited and
     * all inner models will be also finished
     */
    fun finishLifecycle() {
        innerStacks.forEach {
            it.finishAll()
        }
        lifecycle.finish()
    }

    fun innerModel(): ViewModelStack {
        return ViewModelStack().also {
            innerStacks.add(it)
        }
    }

    /**
     * ViewModelProperty of view
     */
    fun <T> value(): ViewModelProperty<T> {
        return ViewModelProperty(this)
    }

    /**
     * ViewModelProperty of view with some default value
     */
    fun <T> value(defaultValue: T): ViewModelProperty<T> {
        return ViewModelProperty<T>(this).apply {
            set(defaultValue)
        }
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

    abstract class SubModel(parent: ViewModel) : ViewModel(parent.lifecycle)

}
