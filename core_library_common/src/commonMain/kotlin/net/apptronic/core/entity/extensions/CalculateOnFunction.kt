package net.apptronic.core.entity.extensions

import net.apptronic.core.entity.commons.BaseProperty

fun <A1, T> ((A1) -> T).calculateOn(
        a1: BaseProperty<A1>
): T {
    return this.invoke(a1.get())
}

fun <A1, A2, T> ((A1, A2) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>
): T {
    return this.invoke(a1.get(), a2.get())
}

fun <A1, A2, A3, T> ((A1, A2, A3) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>,
        a3: BaseProperty<A3>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get())
}

fun <A1, A2, A3, A4, T> ((A1, A2, A3, A4) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>,
        a3: BaseProperty<A3>,
        a4: BaseProperty<A4>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get())
}

fun <A1, A2, A3, A4, A5, T> ((A1, A2, A3, A4, A5) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>,
        a3: BaseProperty<A3>,
        a4: BaseProperty<A4>,
        a5: BaseProperty<A5>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get())
}

fun <A1, A2, A3, A4, A5, A6, T> ((A1, A2, A3, A4, A5, A6) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>,
        a3: BaseProperty<A3>,
        a4: BaseProperty<A4>,
        a5: BaseProperty<A5>,
        a6: BaseProperty<A6>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get())
}

fun <A1, A2, A3, A4, A5, A6, A7, T> ((A1, A2, A3, A4, A5, A6, A7) -> T).calculateOn(
        a1: BaseProperty<A1>,
        a2: BaseProperty<A2>,
        a3: BaseProperty<A3>,
        a4: BaseProperty<A4>,
        a5: BaseProperty<A5>,
        a6: BaseProperty<A6>,
        a7: BaseProperty<A7>
): T {
    return this.invoke(a1.get(), a2.get(), a3.get(), a4.get(), a5.get(), a6.get(), a7.get())
}

fun <T> ((Array<Any?>) -> T).calculateOnArray(
        array: Array<BaseProperty<*>>
): T {
    return this.invoke(array.map { it.get() }.toTypedArray())
}