package net.apptronic.common.core.component.lifecycle

import io.reactivex.disposables.Disposable

interface LifecycleStage {

    fun doOnce(action: () -> Unit)

    /**
     * This function is used to perform single-shot action when lifecycle stage will be entered.
     * It is lifecycle-independent and will not cancelled also if current stage will exit. If
     * current lifecycle stage already entered - action will be executed immediately.
     * It may be useful
     *
     * @param key unique key for action. If action with same key already exists - previous action
     * will be disposed
     * @param action Action to perform when stage will be entered
     */
    fun doOnce(key: String, action: () -> Unit)

    fun doOnEnter(callback: OnEnterHandler.() -> Unit)

    fun doOnExit(callback: OnExitHandler.() -> Unit)

    fun addStage(name: String): LifecycleStage

    interface OnEnterHandler {

        fun Disposable.disposeOnExit()

        fun onExit(callback: OnExitHandler.() -> Unit)

    }

    interface OnExitHandler {

    }

}