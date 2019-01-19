package net.apptronic.common.android.ui.viewmodel.extensions

import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1> doOnChange(
    a1: ViewModelProperty<A1>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    a5: ViewModelProperty<A5>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    a5: ViewModelProperty<A5>,
    a6: ViewModelProperty<A6>,
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
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    a5: ViewModelProperty<A5>,
    a6: ViewModelProperty<A6>,
    a7: ViewModelProperty<A7>,
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
    array: Array<ViewModelProperty<*>>,
    action: (Array<Any?>) -> Unit
) {
    forEachChangeAnyOf(*array) {
        action.executeOnArray(array)
    }
}

