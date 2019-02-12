package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.LiveModelProperty

fun <A1, T> ((A1) -> T).calculateOn(
    a1: LiveModelProperty<A1>
): T {
    return this.invoke(a1.get())
}

fun <A1, A2, T> ((A1, A2) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>
): T {
    return this.invoke(a1.get(), a2.get())
}

fun <A1, A2, A3, T> ((A1, A2, A3) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get())
}

fun <A1, A2, A3, A4, T> ((A1, A2, A3, A4) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get())
}

fun <A1, A2, A3, A4, A5, T> ((A1, A2, A3, A4, A5) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get())
}

fun <A1, A2, A3, A4, A5, A6, T> ((A1, A2, A3, A4, A5, A6) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get())
}

fun <A1, A2, A3, A4, A5, A6, A7, T> ((A1, A2, A3, A4, A5, A6, A7) -> T).calculateOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>,
    a7: LiveModelProperty<A7>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get(), a7.get())
}

fun <T> ((Array<Any?>) -> T).calculateOnArray(
    array: Array<LiveModelProperty<*>>
): T {
    return this.invoke(array.map { it.get() }.toTypedArray())
}