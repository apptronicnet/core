package net.apptronic.core.entity.operators

import net.apptronic.core.entity.base.EntityValue
import net.apptronic.core.entity.base.UpdateEntity

fun <T> T.updateNot() where T : UpdateEntity<Boolean>, T : EntityValue<Boolean> {
    update((getOrNull() ?: false).not())
}