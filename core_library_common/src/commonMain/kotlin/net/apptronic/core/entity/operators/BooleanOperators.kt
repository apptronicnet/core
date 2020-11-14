package net.apptronic.core.entity.operators

import net.apptronic.core.entity.base.MutableValue

fun MutableValue<Boolean>.toggle() {
    update((getOrNull() ?: false).not())
}

