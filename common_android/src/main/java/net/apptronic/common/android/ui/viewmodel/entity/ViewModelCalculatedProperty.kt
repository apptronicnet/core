package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

/**
 * Marker class indicating that property is calculated and should not be set directly
 */
class ViewModelCalculatedProperty<T>(lifecycleHolder: LifecycleHolder<*>) :
    ViewModelProperty<T>(lifecycleHolder)

