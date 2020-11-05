package net.apptronic.core.entity.extensions

import net.apptronic.core.entity.commons.Property

/**
 * ViewModelCalculatedValue af view with value which is calculated from other values
 */
fun <A1> doOnChange(
    a1: Property<A1>,
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
    a1: Property<A1>,
    a2: Property<A2>,
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
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
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
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
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
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>,
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
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>,
    a6: Property<A6>,
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
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>,
    a6: Property<A6>,
    a7: Property<A7>,
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
    array: Array<Property<*>>,
    action: (Array<Any?>) -> Unit
) {
    forEachChangeAnyOf(*array) {
        action.executeOnArray(array)
    }
}

