package net.apptronic.core.entity.operators

import net.apptronic.core.entity.commons.MutableEntity

fun MutableEntity<Boolean>.toggle() {
    update((getOrNull() ?: false).not())
}

