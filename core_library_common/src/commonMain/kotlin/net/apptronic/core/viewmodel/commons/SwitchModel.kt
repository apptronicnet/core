package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.behavior.whenAnyValue
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.and

fun Contextual.switchModel(): SwitchModel {
    return SwitchModel(context)
}

fun Contextual.switchModel(defaultValue: Boolean): SwitchModel {
    return switchModel().apply {
        set(defaultValue)
    }
}

fun Contextual.switchModel(sourceValue: Entity<Boolean>, defaultValue: Boolean = false): SwitchModel {
    return switchModel(defaultValue).apply {
        sourceValue.subscribe(context) {
            set(it)
        }
    }
}

fun SwitchModel.withIsEnabled(isEnabledSource: Entity<Boolean>): SwitchModel {
    isEnabledSource.subscribe(context) {
        isEnabled.set(it)
    }
    return this
}

class SwitchModel internal constructor(context: Context) : MutableValue<Boolean> by BaseMutableValue<Boolean>(context) {

    private val isFilled = whenAnyValue()

    val isEnabled = context.value(true)

    /**
     * Check is initial state is filled
     */
    fun observeIsEnabledAndFilled(): Entity<Boolean> {
        return isFilled and isEnabled
    }

}