package net.apptronic.core.viewmodel.commons

import net.apptronic.core.entity.base.MutableValue

/**
 * Interface which supports enable/disable switching between values.
 */
interface SwitchableSelector<T> {

    fun getSwitch(selection: T): MutableValue<Boolean>

}