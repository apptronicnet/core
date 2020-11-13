package net.apptronic.core.viewmodel.commons

import net.apptronic.core.entity.commons.MutableEntity

/**
 * Interface which supports enable/disable switching between values.
 */
interface SwitchableSelector<T> {

    fun getSwitch(selection: T): MutableEntity<Boolean>

}