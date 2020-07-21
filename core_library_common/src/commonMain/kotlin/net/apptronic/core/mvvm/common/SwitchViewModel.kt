package net.apptronic.core.mvvm.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.component.coroutines.lifecycleCoroutineScope
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.behavior.whenAnyValue
import net.apptronic.core.component.entity.entities.setTo
import net.apptronic.core.component.entity.functions.and
import net.apptronic.core.component.entity.functions.onNext
import net.apptronic.core.component.entity.functions.onNextSuspend
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun ViewModel.switchModel(): SwitchViewModel {
    return SwitchViewModel(context)
}

fun ViewModel.switchModel(defaultValue: Boolean): SwitchViewModel {
    return switchModel().apply {
        setState(defaultValue)
    }
}

fun ViewModel.switchModel(sourceValue: Entity<Boolean>, defaultValue: Boolean = false): SwitchViewModel {
    return switchModel(defaultValue).apply {
        sourceValue.subscribe(context) {
            setState(it)
        }
    }
}

/**
 * Load initial state of switch. If [load] returns null state will no be set
 */
fun SwitchViewModel.withLoadState(load: () -> Boolean?): SwitchViewModel {
    load()?.let {
        setState(it)
    }
    return this
}


/**
 * Load initial state of switch. If [load] returns null state will no be set
 */
fun SwitchViewModel.withLoadStateSuspend(load: suspend CoroutineScope.() -> Boolean?): SwitchViewModel {
    lifecycleCoroutineScope.launch {
        load()?.let {
            setState(it)
        }
    }
    return this
}

fun SwitchViewModel.withOnUpdate(onUpdate: (Boolean) -> Unit): SwitchViewModel {
    observeUpdates().onNext(onUpdate)
    return this
}

fun SwitchViewModel.withOnUpdateSuspend(onUpdate: suspend CoroutineScope.(Boolean) -> Unit): SwitchViewModel {
    observeUpdates().onNextSuspend(onUpdate)
    return this
}


fun SwitchViewModel.withSendUpdates(target: UpdateEntity<Boolean>): SwitchViewModel {
    observeUpdates().setTo(target)
    return this
}

fun SwitchViewModel.withIsEnabled(isEnabled: Entity<Boolean>): SwitchViewModel {
    isEnabled.subscribe(context) {
        setEnabled(it)
    }
    return this
}

class SwitchViewModel internal constructor(context: ViewModelContext) : ViewModel(context) {

    private val state = value<Boolean>()
    private val updateEvent = typedEvent<Boolean>()
    private val isFilled = state.whenAnyValue()
    private val isEnabled = value(true)

    /**
     * Get current state of switch
     */
    fun getState(): Boolean {
        return state.get()
    }

    /**
     * Get current state of switch or null if state was not set yet
     */
    fun getStateOrNull(): Boolean? {
        return state.getOrNull()
    }

    fun getEnabled(): Boolean {
        return isEnabled.get()
    }

    /**
     * Set initial/changed state of switch
     */
    fun setState(nextState: Boolean) {
        state.set(nextState)
    }

    /**
     * Send state update from user
     */
    fun updateState(updatedState: Boolean) {
        if (state.getOrNull() != updatedState) {
            state.set(updatedState)
            updateEvent.sendEvent(updatedState)
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled.set(enabled)
    }

    /**
     * Observe current state of switch
     */
    fun observeState(): Entity<Boolean> {
        return state
    }

    /**
     * Check is initial state is filled
     */
    fun observeIsEnabledAndFilled(): Entity<Boolean> {
        return isFilled and isEnabled
    }

    /**
     * Observe when switch is update by user
     */
    fun observeUpdates(): Entity<Boolean> {
        return updateEvent
    }

}