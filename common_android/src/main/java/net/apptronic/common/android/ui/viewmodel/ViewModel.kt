package net.apptronic.common.android.ui.viewmodel

import android.content.Context
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelGenericEvent
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelTypedEvent
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModel {

    private val lifecycleHolder: LifecycleHolder<*>
    val context: ViewModelProperty<Context>

    constructor(lifecycleHolder: LifecycleHolder<*>) {
        this.lifecycleHolder = lifecycleHolder
        context = value()
    }

    constructor(parent: ViewModel) {
        this.lifecycleHolder = parent.lifecycleHolder
        context = parent.context
    }

    /**
     * ViewModelProperty of view
     */
    fun <T> value(): ViewModelProperty<T> {
        return ViewModelProperty(lifecycleHolder)
    }

    /**
     * ViewModelProperty of view with some default value
     */
    fun <T> value(defaultValue: T): ViewModelProperty<T> {
        return ViewModelProperty<T>(lifecycleHolder).apply {
            set(defaultValue)
        }
    }

    /**
     * User action on screen
     */
    fun genericEvent(): ViewModelGenericEvent {
        return ViewModelGenericEvent(lifecycleHolder)
    }

    /**
     * User action on screen
     */
    fun <T> typedEvent(): ViewModelTypedEvent<T> {
        return ViewModelTypedEvent(lifecycleHolder)
    }

    abstract class SubModel(parent: ViewModel) : ViewModel(parent)

}
