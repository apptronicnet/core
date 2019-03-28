package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.Component
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.mvvm.lifecycle.LifecycleController

open class ViewModel(context: ViewModelContext) : Component(context) {

    private val lifecycleController = context.lifecycleController

    init {
        lifecycleController.bindTarget(this)
        context.initLifecycleLogging(this)
    }

    fun getLifecycleController(): LifecycleController {
        return lifecycleController
    }

    private val innerContainers = mutableListOf<ViewModelContainer>()
    /**
     * Called to end lifecycle for this view model: lifecycle will be forced to be exited and
     * all inner models will be also finished
     */
    fun finishLifecycle() {
        innerContainers.forEach {
            it.finishAll()
        }
        getLifecycle().finish()
    }

    fun subModelContainer(): ViewModelContainer {
        return ViewModelContainer(this).also {
            innerContainers.add(it)
        }
    }

    internal fun onAddedToContainer(parent: ViewModelParent) {
        context.getLifecycle().enterStage(ViewModelLifecycle.STAGE_CREATED)
    }

    internal fun onRemovedFromContainer(parent: ViewModelParent) {
        context.getLifecycle().exitStage(ViewModelLifecycle.STAGE_CREATED)
    }

    private var parent: ViewModelParent? = null

    fun onAttachToParent(parent: ViewModelParent) {
        this.parent = parent
    }

    fun onDetachFromParent() {
        this.parent = null
    }

    /**
     * Request [ViewModelParent] ti close this [ViewModel]
     * @return true is parent exists and false otherwise
     */
    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            workers().execute(context.workers().getDefaultWorker()) {
                it.requestCloseSelf(this, transitionInfo)
            }
            true
        } ?: false
    }

    // ----- CREATED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_CREATED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_CREATED, callback)
    }

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_CREATED]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceCreated(key: String = "", action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_CREATED, key, action)
    }

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_CREATED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_CREATED, callback)
    }

    // ----- BOUND STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_BOUND]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnBind(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_BOUND, callback)
    }

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_BOUND]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceBound(key: String = "", action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_BOUND, key, action)
    }

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_BOUND]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnbind(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_BOUND, callback)
    }

    // ----- VISIBLE STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_VISIBLE]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnVisible(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_VISIBLE, callback)
    }

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_VISIBLE]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceVisible(key: String = "", action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_VISIBLE, key, action)
    }

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_VISIBLE]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnHidden(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_VISIBLE, callback)
    }

    // ----- FOCUSED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_FOCUSED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnFocused(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_FOCUSED, callback)
    }

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_FOCUSED]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceFocused(key: String = "", action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_FOCUSED, key, action)
    }

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_FOCUSED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnfocused(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_FOCUSED, callback)
    }

}