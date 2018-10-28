package net.apptronic.common.android.ui.viewmodel

import android.content.Context
import net.apptronic.common.android.ui.viewmodel.entity.Property
import net.apptronic.common.android.ui.viewmodel.entity.ViewEvent
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModel {

    private val lifecycleHolder: LifecycleHolder<*>
    val context: Property<Context>

    constructor(lifecycleHolder: LifecycleHolder<*>) {
        this.lifecycleHolder = lifecycleHolder
        context = value()
    }

    constructor(parent: ViewModel) {
        this.lifecycleHolder = parent.lifecycleHolder
        context = parent.context
    }

    /**
     * Property of view
     */
    fun <T> value(): Property<T> {
        return Property(lifecycleHolder)
    }

    /**
     * Property of view with some default value
     */
    fun <T> value(defaultValue: T): Property<T> {
        return Property<T>(lifecycleHolder).apply {
            set(defaultValue)
        }
    }

    /**
     * User action on screen
     */
    fun genericEvent(): ViewEvent<Unit> {
        return ViewEvent(lifecycleHolder)
    }

    /**
     * User action on screen
     */
    fun <T> typedEvent(): ViewEvent<T> {
        return ViewEvent(lifecycleHolder)
    }

    abstract class SubModel(parent: ViewModel) : ViewModel(parent)

}
