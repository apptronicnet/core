package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.Property

fun <E : Property<T>, T, A1> E.asFunctionOf(
    a1: Property<A1>,
    functionOf: (A1) -> T
): E {
    forEachChangeAnyOf(a1) {
        this.set(functionOf.calculateOn(a1))
    }
    return this
}

fun <E : Property<T>, T, A1, A2> E.asFunctionOf(
    a1: Property<A1>,
    a2: Property<A2>,
    functionOf: (A1, A2) -> T
): E {
    forEachChangeAnyOf(a1, a2) {
        this.set(functionOf.calculateOn(a1, a2))
    }
    return this
}

fun <E : Property<T>, T, A1, A2, A3> E.asFunctionOf(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    functionOf: (A1, A2, A3) -> T
): E {
    forEachChangeAnyOf(a1, a2, a3) {
        this.set(functionOf.calculateOn(a1, a2, a3))
    }
    return this
}


fun <E : Property<T>, T, A1, A2, A3, A4> E.asFunctionOf(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    functionOf: (A1, A2, A3, A4) -> T
): E {
    forEachChangeAnyOf(a1, a2, a3, a4) {
        this.set(functionOf.calculateOn(a1, a2, a3, a4))
    }
    return this
}

fun <E : Property<T>, T> E.asFunctionOf(
    array: Array<Property<*>>,
    functionOf: (Array<Any?>) -> T
) {
    forEachChangeAnyOf(*array) {
        this.set(functionOf.calculateOnArray(array))
    }
}

