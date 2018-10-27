package net.apptronic.common.android.ui.viewmodel

import net.apptronic.common.android.ui.viewmodel.entity.UserAction
import net.apptronic.common.android.ui.viewmodel.entity.ValueProperty
import net.apptronic.common.android.ui.viewmodel.entity.ViewProperty
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModel(private val lifecycleHolder: LifecycleHolder<*>) {

    /**
     * Property of view
     */
    fun <T> property(): ViewProperty<T> {
        return ViewProperty(lifecycleHolder)
    }

    /**
     * Property of view with some default value
     */
    fun <T> property(defaultValue: T): ViewProperty<T> {
        return ViewProperty<T>(lifecycleHolder).apply {
            set(defaultValue)
        }
    }

    /**
     * State value of screen
     */
    fun <T> value(): ValueProperty<T> {
        return ValueProperty(lifecycleHolder)
    }

    /**
     * State value  of screen with default value
     */
    fun <T> value(defaultValue: T): ValueProperty<T> {
        return ValueProperty<T>(lifecycleHolder).apply {
            set(defaultValue)
        }
    }

    /**
     * User action on screen
     */
    fun <T> userAction(): UserAction<T> {
        return UserAction(lifecycleHolder)
    }

}
