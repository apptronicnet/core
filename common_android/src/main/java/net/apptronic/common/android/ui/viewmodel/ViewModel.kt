package net.apptronic.common.android.ui.viewmodel

import net.apptronic.common.android.ui.viewmodel.entity.StateProperty
import net.apptronic.common.android.ui.viewmodel.entity.UserAction
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
     * State of screen
     */
    fun <T> state(): StateProperty<T> {
        return StateProperty(lifecycleHolder)
    }

    /**
     * State of screen with default value
     */
    fun <T> state(defaultValue: T): StateProperty<T> {
        return StateProperty<T>(lifecycleHolder).apply {
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
