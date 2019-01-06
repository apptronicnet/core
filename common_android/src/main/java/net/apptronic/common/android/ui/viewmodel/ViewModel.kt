package net.apptronic.common.android.ui.viewmodel

import android.content.Context
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelGenericEvent
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelTypedEvent
import net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions.forEachChange
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
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1> function(
        a1: ViewModelProperty<A1>,
        function: (A1) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1) {
                set(function(a1.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        function: (A1, A2) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2) {
                set(function(a1.get(), a2.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2, A3> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        a3: ViewModelProperty<A3>,
        function: (A1, A2, A3) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2, a3) {
                set(function(a1.get(), a2.get(), a3.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2, A3, A4> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        a3: ViewModelProperty<A3>,
        a4: ViewModelProperty<A4>,
        function: (A1, A2, A3, A4) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2, a3, a4) {
                set(function(a1.get(), a2.get(), a3.get(), a4.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2, A3, A4, A5> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        a3: ViewModelProperty<A3>,
        a4: ViewModelProperty<A4>,
        a5: ViewModelProperty<A5>,
        function: (A1, A2, A3, A4, A5) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2, a3, a4, a5) {
                set(function(a1.get(), a2.get(), a3.get(), a4.get(), a5.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2, A3, A4, A5, A6> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        a3: ViewModelProperty<A3>,
        a4: ViewModelProperty<A4>,
        a5: ViewModelProperty<A5>,
        a6: ViewModelProperty<A6>,
        function: (A1, A2, A3, A4, A5, A6) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2, a3, a4, a5, a6) {
                set(function(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T, A1, A2, A3, A4, A5, A6, A7> function(
        a1: ViewModelProperty<A1>,
        a2: ViewModelProperty<A2>,
        a3: ViewModelProperty<A3>,
        a4: ViewModelProperty<A4>,
        a5: ViewModelProperty<A5>,
        a6: ViewModelProperty<A6>,
        a7: ViewModelProperty<A7>,
        function: (A1, A2, A3, A4, A5, A6, A7) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(a1, a2, a3, a4, a5, a6, a7) {
                set(function(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get(), a7.get()))
            }
        }
    }

    /**
     * ViewModelCalculatedProperty af view with value which is calculated from other values
     */
    fun <T> function(
        array: Array<ViewModelProperty<*>>,
        function: (Array<Any?>) -> T
    ): ViewModelProperty<T> {
        return value<T>().apply {
            forEachChange(*array) {
                set(
                    function(
                        array.map { it.get() }.toTypedArray()
                    )
                )
            }
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
