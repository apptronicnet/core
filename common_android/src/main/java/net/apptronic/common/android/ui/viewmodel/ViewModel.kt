package net.apptronic.common.android.ui.viewmodel

import net.apptronic.common.android.ui.viewmodel.adapter.ViewModelStack
import net.apptronic.common.android.ui.viewmodel.entity.*
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage
import java.util.*

abstract class ViewModel(private val lifecycle: Lifecycle) : LifecycleHolder {

    private val id: Long = ViewModelRegistry.nextId()
    private val innerStacks = LinkedList<ViewModelStack>()
    private var parent: ViewModelParent? = null

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
        return lifecycle
    }

    init {
        ViewModelRegistry.add(this@ViewModel)
        doOnTerminate {
            ViewModelRegistry.remove(this@ViewModel)
        }
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

    fun modelStack(): ViewModelStack {
        return ViewModelStack(this).also {
            innerStacks.add(it)
        }
    }

    /**
     * ViewModelProperty of view
     */
    fun <T> value(): ViewModelProperty<T> {
        return ViewModelValue(this)
    }

    /**
     * ViewModelProperty of view with some default value
     */
    fun <T> value(defaultValue: T): ViewModelProperty<T> {
        return ViewModelValue<T>(this).apply {
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

    fun <T> resultListener(onReceived: (T) -> Unit): ResultListener<T> {
        val resultEvent = typedEvent<T>()
        resultEvent.subscribe(onReceived)
        return object : ResultListener<T> {
            override fun setResult(result: T) {
                lifecycle.provideThreadExecutor().execute {
                    resultEvent.sendEvent(result)
                }
            }
        }

    }

    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            lifecycle.provideThreadExecutor().execute {
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
        lifecycle.provideThreadExecutor().execute(block)
    }


}
