package net.apptronic.core.context.lifecycle

import net.apptronic.core.context.plugin.Extendable
import net.apptronic.core.entity.EntitySubscription

interface LifecycleStage : Extendable {

    fun doOnce(action: () -> Unit)

    fun isEntered(): Boolean

    /**
     * This function is used to perform single-shot action when lifecycle stage will be entered.
     * It is lifecycle-independent and will not be cancelled also if current stage will exit. If
     * this lifecycle stage already entered - action will be executed immediately.
     * It may be useful
     *
     * @param key unique key for action. If action with same key already exists - previous action
     * will be disposed
     * @param action Action to perform when stage will be entered
     */
    fun doOnce(key: String, action: () -> Unit): LifecycleSubscription

    /**
     * Perform some action when lifecycle will enter to this stage. This action will be executed
     * each time stage entering until current active stage (at moment of this method call) exited.
     * Note, that is should be called before this stage entered, in other case it will not
     * have any effect.
     */
    fun doOnEnter(callback: OnEnterHandler.() -> Unit): LifecycleSubscription

    /**
     * Perform some action when lifecycle will exit from this stage. This action will be executed
     * each time stage entering until current active stage (at moment of this method call) exited.
     */
    fun doOnExit(callback: OnExitHandler.() -> Unit): LifecycleSubscription

    fun registerSubscription(subscription: EntitySubscription)

    /**
     * Interface provided at the moment of [doOnEnter] subscription
     */
    interface OnEnterHandler {

        /**
         * Perform some action when current stage will be exited. As this is called at the moment
         * on stage enter this will be called once and unsubscribed.
         */
        fun onExit(callback: OnExitHandler.() -> Unit): LifecycleSubscription

    }

    /**
     * Interface provided at the moment of [doOnExit] subscription
     */
    interface OnExitHandler {

    }

    fun getStageDefinition(): LifecycleStageDefinition

    fun getStageName(): String

}