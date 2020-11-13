package net.apptronic.core.viewmodel.commons

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.UpdateEntity
import net.apptronic.core.entity.behavior.whenAnyValue
import net.apptronic.core.entity.commons.*
import net.apptronic.core.entity.function.and
import net.apptronic.core.entity.function.onNext
import net.apptronic.core.entity.function.onNextSuspend

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

/**
 * Load initial state of switch. If [load] returns null state will no be set
 */
fun SwitchModel.withLoadState(load: () -> Boolean?): SwitchModel {
    load()?.let {
        set(it)
    }
    return this
}


/**
 * Load initial state of switch. If [load] returns null state will no be set
 */
fun SwitchModel.withLoadStateSuspend(load: suspend CoroutineScope.() -> Boolean?): SwitchModel {
    context.lifecycleCoroutineScope.launch {
        load()?.let {
            set(it)
        }
    }
    return this
}

fun SwitchModel.withOnUpdate(onUpdate: (Boolean) -> Unit): SwitchModel {
    updates.onNext(onUpdate)
    return this
}

fun SwitchModel.withOnUpdateSuspend(onUpdate: suspend CoroutineScope.(Boolean) -> Unit): SwitchModel {
    updates.onNextSuspend(onUpdate)
    return this
}


fun SwitchModel.withSendUpdates(target: UpdateEntity<Boolean>): SwitchModel {
    updates.setTo(target)
    return this
}

fun SwitchModel.withIsEnabled(isEnabledSource: Entity<Boolean>): SwitchModel {
    isEnabledSource.subscribe(context) {
        isEnabled.set(it)
    }
    return this
}

class SwitchModel internal constructor(context: Context) : MutableEntity<Boolean> by Value<Boolean>(context) {

    private val isFilled = whenAnyValue()

    val isEnabled = context.value(true)

    /**
     * Check is initial state is filled
     */
    fun observeIsEnabledAndFilled(): Entity<Boolean> {
        return isFilled and isEnabled
    }

}