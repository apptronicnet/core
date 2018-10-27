package net.apptronic.common.android.ui.viewmodel

import net.apptronic.common.android.ui.viewmodel.entity.Property
import net.apptronic.common.android.ui.viewmodel.entity.UserAction
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModel(private val lifecycleHolder: LifecycleHolder<*>) {

    fun <T> property(): Property<T> {
        return Property(lifecycleHolder)
    }

    fun <T> property(defaultValue: T): Property<T> {
        return Property<T>(lifecycleHolder).apply {
            set(defaultValue)
        }
    }

    fun <T> userAction(): UserAction<T> {
        return UserAction(lifecycleHolder)
    }

}
