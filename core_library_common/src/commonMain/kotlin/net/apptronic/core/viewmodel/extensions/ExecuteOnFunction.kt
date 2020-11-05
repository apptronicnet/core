package net.apptronic.core.viewmodel.extensions

import net.apptronic.core.entity.entities.Property

fun <A1> ((A1) -> Unit).executeOn(
    a1: Property<A1>
) {
    this.invoke(a1.get())
}

fun <A1, A2> ((A1, A2) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>
) {
    this.invoke(a1.get(), a2.get())
}

fun <A1, A2, A3> ((A1, A2, A3) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>
) {
    this.invoke(a1.get(), a2.get(), a3.get())
}

fun <A1, A2, A3, A4> ((A1, A2, A3, A4) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get())
}

fun <A1, A2, A3, A4, A5> ((A1, A2, A3, A4, A5) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get())
}

fun <A1, A2, A3, A4, A5, A6> ((A1, A2, A3, A4, A5, A6) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>,
    a6: Property<A6>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get())
}

fun <A1, A2, A3, A4, A5, A6, A7> ((A1, A2, A3, A4, A5, A6, A7) -> Unit).executeOn(
    a1: Property<A1>,
    a2: Property<A2>,
    a3: Property<A3>,
    a4: Property<A4>,
    a5: Property<A5>,
    a6: Property<A6>,
    a7: Property<A7>
) {
    this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get(), a7.get())
}

fun ((Array<Any?>) -> Unit).executeOnArray(
    array: Array<Property<*>>
) {
    this.invoke(array.map { it.get() }.toTypedArray())
}