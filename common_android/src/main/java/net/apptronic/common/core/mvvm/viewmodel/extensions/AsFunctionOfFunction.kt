package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.ViewModelProperty

fun <E : ViewModelProperty<T>, T, A1> E.asFunctionOf(
    a1: ViewModelProperty<A1>,
    functionOf: (A1) -> T
): E {
    forEachChangeAnyOf(a1) {
        this.set(functionOf.calculateOn(a1))
    }
    return this
}

fun <E : ViewModelProperty<T>, T, A1, A2> E.asFunctionOf(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    functionOf: (A1, A2) -> T
): E {
    forEachChangeAnyOf(a1, a2) {
        this.set(functionOf.calculateOn(a1, a2))
    }
    return this
}

fun <E : ViewModelProperty<T>, T, A1, A2, A3> E.asFunctionOf(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    functionOf: (A1, A2, A3) -> T
): E {
    forEachChangeAnyOf(a1, a2, a3) {
        this.set(functionOf.calculateOn(a1, a2, a3))
    }
    return this
}


fun <E : ViewModelProperty<T>, T, A1, A2, A3, A4> E.asFunctionOf(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    functionOf: (A1, A2, A3, A4) -> T
): E {
    forEachChangeAnyOf(a1, a2, a3, a4) {
        this.set(functionOf.calculateOn(a1, a2, a3, a4))
    }
    return this
}

fun <E : ViewModelProperty<T>, T> E.asFunctionOf(
    array: Array<ViewModelProperty<*>>,
    functionOf: (Array<Any?>) -> T
) {
    forEachChangeAnyOf(*array) {
        this.set(functionOf.calculateOnArray(array))
    }
}

