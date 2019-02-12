package net.apptronic.common.core.mvvm.viewmodel.extensions

import net.apptronic.common.core.component.entity.entities.LiveModelProperty

fun <A1> ((A1) -> Unit).executeOn(
    a1: LiveModelProperty<A1>
) {
    this.invoke(a1.get())
}

fun <A1, A2> ((A1, A2) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>
) {
    this.invoke(a1.get(), a2.get())
}

fun <A1, A2, A3> ((A1, A2, A3) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>
) {
    this.invoke(a1.get(), a2.get(), a3.get())
}

fun <A1, A2, A3, A4> ((A1, A2, A3, A4) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get())
}

fun <A1, A2, A3, A4, A5> ((A1, A2, A3, A4, A5) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get())
}

fun <A1, A2, A3, A4, A5, A6> ((A1, A2, A3, A4, A5, A6) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get())
}

fun <A1, A2, A3, A4, A5, A6, A7> ((A1, A2, A3, A4, A5, A6, A7) -> Unit).executeOn(
    a1: LiveModelProperty<A1>,
    a2: LiveModelProperty<A2>,
    a3: LiveModelProperty<A3>,
    a4: LiveModelProperty<A4>,
    a5: LiveModelProperty<A5>,
    a6: LiveModelProperty<A6>,
    a7: LiveModelProperty<A7>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get(), a7.get())
}

fun ((Array<Any?>) -> Unit).executeOnArray(
    array: Array<LiveModelProperty<*>>
) {
    this.invoke(array.map { it.get() }.toTypedArray())
}