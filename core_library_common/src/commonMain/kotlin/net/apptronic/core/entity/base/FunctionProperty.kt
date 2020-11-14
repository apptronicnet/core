package net.apptronic.core.entity.base

import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.entity.commons.distinctUntilChanged

/**
 * Type of [Property] which is a result of some calculations inside. The main difference from property
 * is that it can emit same values one after another.
 */
interface FunctionProperty<T> : Property<T> {

    /**
     * Convert to classical property. Equivalent of [distinctUntilChanged] and converting to [Property]
     */
    fun distinct(eqComparator: EqComparator<T> = SimpleEqComparator()): Property<T>

}

