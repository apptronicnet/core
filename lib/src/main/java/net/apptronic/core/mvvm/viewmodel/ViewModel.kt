package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.base.ComponentLoggerDescriptor
import net.apptronic.core.component.Component
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.base.UpdatePredicate
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.mvvm.viewmodel.container.ViewModelStackNavigator

open class ViewModel : Component {

    private val viewModelContext: ViewModelContext

    constructor(context: ViewModelContext) : super(context) {
        viewModelContext = context
    }

    constructor(parent: ViewModel) : super(parent.viewModelContext) {
        viewModelContext = parent.viewModelContext
    }

    private val logger = getProvider().inject(ComponentLoggerDescriptor)

    override fun getDefaultWorker(): String {
        return ContextWorkers.UI
    }

    init {
        getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnce {
            logger.log("ViewModelLifecycle: $this initialized")
        }
        doOnTerminate {
            logger.log("ViewModelLifecycle: ${this@ViewModel} terminated")
        }
    }

    private val isCreated = stateOfStage(ViewModelLifecycle.STAGE_CREATED)
    private val isBound = stateOfStage(ViewModelLifecycle.STAGE_BOUND)
    private val isVisible = stateOfStage(ViewModelLifecycle.STAGE_VISIBLE)
    private val isFocused = stateOfStage(ViewModelLifecycle.STAGE_FOCUSED)

    private fun stateOfStage(stageName: String): UpdatePredicate<Boolean> {
        val target = UpdateAndStorePredicate<Boolean>()
        onEnterStage(stageName) {
            logger.log("ViewModelLifecycle: ${this@ViewModel} entered stage$stageName")
            target.update(true)
        }
        onExitStage(stageName) {
            target.update(false)
            logger.log("ViewModelLifecycle: ${this@ViewModel} exited stage$stageName")
        }
        return target
    }

    fun stackNavigator(): ViewModelStackNavigator {
        return ViewModelStackNavigator(this)
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

    fun isCreated() = getLifecycle().isStageEntered(ViewModelLifecycle.STAGE_CREATED)

    fun observeCreated(): Predicate<Boolean> = isCreated

    fun isBound() = getLifecycle().isStageEntered(ViewModelLifecycle.STAGE_BOUND)

    fun observeBound(): Predicate<Boolean> = isBound

    fun isVisible() = getLifecycle().isStageEntered(ViewModelLifecycle.STAGE_VISIBLE)

    fun observeVisible(): Predicate<Boolean> = isVisible

    fun isFocused() = getLifecycle().isStageEntered(ViewModelLifecycle.STAGE_FOCUSED)

    fun observeFocused(): Predicate<Boolean> = isFocused

    /**
     * Check is current state is exactly on state created
     */
    fun isStateCreated(): Boolean {
        return isCreated() && !isBound()
    }

    /**
     * Check is current state is exactly on state bound
     */
    fun isStateBound(): Boolean {
        return isBound() && !isVisible()
    }

    /**
     * Check is current state is exactly on state visible
     */
    fun isStateVisible(): Boolean {
        return isVisible() && !isFocused()
    }

    /**
     * Check is current state is exactly on state focused
     */
    fun isStateFocused(): Boolean {
        return isFocused()
    }

    /**
     * Check is current [ViewModel] and it's [ViewModelLifecycle] is terminated
     */
    fun isTerminated(): Boolean {
        return getLifecycle().isTerminated()
    }

    /**
     * Request [ViewModelParent] ti close this [ViewModel]
     * @return true is parent exists and false otherwise
     */
    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            getWorkers().execute(context.getWorkers().getDefaultWorker()) {
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