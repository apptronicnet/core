package net.apptronic.common.android.ui.viewmodel.lifecycle

import net.apptronic.common.android.ui.threading.ThreadExecutor

interface LifecycleHolder<T : Lifecycle> {

    fun localLifecycle(): T

    fun threadExecutor(): ThreadExecutor

}