package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.LiveModelProperty

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1> doOnChange(
    a1: LiveModelProperty<A1>,
    action: (A1) -> Unit
) {
    forEachChangeAnyOf(a1) {
        action.executeOn(a1)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    action: (A1, A2) -> Unit
) {
    forEachChangeAnyOf(a1, a2) {
        action.executeOn(a1, a2)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2, A3> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    action: (A1, A2, A3) -> Unit
) {
    forEachChangeAnyOf(a1, a2, a3) {
        action.executeOn(a1, a2, a3)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2, A3, A4> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    action: (A1, A2, A3, A4) -> Unit
) {
    forEachChangeAnyOf(a1, a2, a3, a4) {
        action.executeOn(a1, a2, a3, a4)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2, A3, A4, A5> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    action: (A1, A2, A3, A4, A5) -> Unit
) {
    forEachChangeAnyOf(a1, a2, a3, a4, a5) {
        action.executeOn(a1, a2, a3, a4, a5)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2, A3, A4, A5, A6> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>,
    action: (A1, A2, A3, A4, A5, A6) -> Unit
) {
    forEachChangeAnyOf(a1, a2, a3, a4, a5, a6) {
        action.executeOn(a1, a2, a3, a4, a5, a6)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1, A2, A3, A4, A5, A6, A7> doOnChange(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>,
    a7: LiveModelProperty<A7>,
    action: (A1, A2, A3, A4, A5, A6, A7) -> Unit
) {
    forEachChangeAnyOf(a1, a2, a3, a4, a5, a6, a7) {
        action.executeOn(a1, a2, a3, a4, a5, a6, a7)
    }
}

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <T> doOnChange(
    array: Array<LiveModelProperty<*>>,
    action: (Array<Any?>) -> Unit
) {
    forEachChangeAnyOf(*array) {
        action.executeOnArray(array)
    }
}

