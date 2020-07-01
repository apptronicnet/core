package net.apptronic.core.component.entity.updaters

import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.UpdateEntity

fun <T> T.updateNot() where T : UpdateEntity<Boolean>, T : EntityValue<Boolean> {
    update((getOrNull() ?: false).not())
}