package net.apptronic.core.viewmodel

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextDefinition
import net.apptronic.core.context.component.AbstractComponent
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.component.addComponent
import net.apptronic.core.context.component.genericEvent
import net.apptronic.core.context.lifecycle.LifecycleStage
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.behavior.doWhen
import net.apptronic.core.entity.bindContext

interface IViewModel : IComponent {

    override val context: ViewModelContext

    fun onAttachToParent(parent: ViewModelParent)

    fun onDetachFromParent()

    fun isAttached(): Boolean

    fun observeAttached(): Observable<Boolean>

    fun isBound(): Boolean

    fun observeBound(): Observable<Boolean>

    fun isVisible(): Boolean

    fun observeVisible(): Observable<Boolean>

    fun isFocused(): Boolean

    fun observeFocused(): Observable<Boolean>

    /**
     * Create [Entity] which emits item when [ViewModel] entering attached stage
     */
    fun whenAttached(): Entity<Unit>

    /**
     * Create [Entity] which emits item when [ViewModel] entering bound stage
     */
    fun whenBound(): Entity<Unit>

    /**
     * Create [Entity] which emits item when [ViewModel] entering visible stage
     */
    fun whenVisible(): Entity<Unit>

    /**
     * Create [Entity] which emits item when [ViewModel] entering focused stage
     */
    fun whenFocused(): Entity<Unit>

    /**
     * Check is current state is exactly on state attached
     */
    fun isStateAttached(): Boolean

    /**
     * Check is current state is exactly on state bound
     */
    fun isStateBound(): Boolean

    /**
     * Check is current state is exactly on state visible
     */
    fun isStateVisible(): Boolean

    /**
     * Check is current state is exactly on state focused
     */
    fun isStateFocused(): Boolean

    /**
     * Check is current [ViewModel] and it's [ViewModelLifecycle] is terminated
     */
    fun isTerminated(): Boolean

    /**
     * Request [ViewModelParent] ti close this [ViewModel]
     * @return true is parent exists and false otherwise
     */
    fun closeSelf(transitionInfo: Any? = null): Boolean

    // ----- CREATED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_ATTACHED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnAttach(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_ATTACHED]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceAttached(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_ATTACHED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnDetach(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- BOUND STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_BOUND]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnBind(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_BOUND]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceBound(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_BOUND]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnbind(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- VISIBLE STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_VISIBLE]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnVisible(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_VISIBLE]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceVisible(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_VISIBLE]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnHidden(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- FOCUSED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_FOCUSED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnFocused(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_FOCUSED]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceFocused(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_FOCUSED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnfocused(callback: LifecycleStage.OnExitHandler.() -> Unit)

}

open class ViewModel : AbstractComponent, IViewModel {

    final override val context: ViewModelContext

    private val isAttached = BehaviorSubject<Boolean>()
    private val isBound = BehaviorSubject<Boolean>()
    private val isVisible = BehaviorSubject<Boolean>()
    private val isFocused = BehaviorSubject<Boolean>()

    private var parent: ViewModelParent? = null

    constructor(context: ViewModelContext) {
        this.context = context
        doInit()
    }

    constructor(parent: IViewModel) {
        context = parent.context
        doInit()
    }

    constructor(parent: Context, contextDefinition: ContextDefinition<ViewModelContext>) {
        context = contextDefinition.createContext(parent)
        doInit()
    }

    private fun doInit() {
        stateOfStage(isAttached, ViewModelLifecycle.STAGE_ATTACHED)
        stateOfStage(isBound, ViewModelLifecycle.STAGE_BOUND)
        stateOfStage(isVisible, ViewModelLifecycle.STAGE_VISIBLE)
        stateOfStage(isFocused, ViewModelLifecycle.STAGE_FOCUSED)
        context.addComponent(this)
    }

    private fun stateOfStage(target: BehaviorSubject<Boolean>, definition: LifecycleStageDefinition): Observable<Boolean> {
        onEnterStage(definition) {
            target.update(true)
        }
        onExitStage(definition) {
            target.update(false)
        }
        return target
    }

    final override fun onAttachToParent(parent: ViewModelParent) {
        context.lifecycle.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        this.parent = parent
    }

    final override fun onDetachFromParent() {
        context.lifecycle.exitStage(ViewModelLifecycle.STAGE_ATTACHED)
        this.parent = null
    }

    final override fun isAttached() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_ATTACHED)

    final override fun observeAttached(): Observable<Boolean> = isAttached

    final override fun isBound() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_BOUND)

    final override fun observeBound(): Observable<Boolean> = isBound

    final override fun isVisible() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_VISIBLE)

    final override fun observeVisible(): Observable<Boolean> = isVisible

    final override fun isFocused() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_FOCUSED)

    final override fun observeFocused(): Observable<Boolean> = isFocused

    private fun whenEntered(observable: Observable<Boolean>): Entity<Unit> {
        return genericEvent().also { event ->
            doWhen(observable.bindContext(context)) {
                event.sendEvent()
            }
        }
    }

    final override fun whenAttached(): Entity<Unit> {
        return whenEntered(isAttached)
    }

    final override fun whenBound(): Entity<Unit> {
        return whenEntered(isBound)
    }

    final override fun whenVisible(): Entity<Unit> {
        return whenEntered(isVisible)
    }

    final override fun whenFocused(): Entity<Unit> {
        return whenEntered(isFocused)
    }

    final override fun isStateAttached(): Boolean {
        return isAttached() && !isBound()
    }

    final override fun isStateBound(): Boolean {
        return isBound() && !isVisible()
    }

    final override fun isStateVisible(): Boolean {
        return isVisible() && !isFocused()
    }

    final override fun isStateFocused(): Boolean {
        return isFocused()
    }

    final override fun isTerminated(): Boolean {
        return context.lifecycle.isTerminated()
    }

    final override fun closeSelf(transitionInfo: Any?): Boolean {
        return parent?.let {
            it.requestCloseSelf(this, transitionInfo)
            true
        } ?: false
    }

    final override fun doOnAttach(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_ATTACHED, callback)
    }

    final override fun onceAttached(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_ATTACHED, key, action)
    }

    final override fun doOnDetach(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_ATTACHED, callback)
    }

    // ----- BOUND STAGE ACTIONS -----

    final override fun doOnBind(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_BOUND, callback)
    }

    final override fun onceBound(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_BOUND, key, action)
    }

    final override fun doOnUnbind(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_BOUND, callback)
    }

    // ----- VISIBLE STAGE ACTIONS -----

    final override fun doOnVisible(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_VISIBLE, callback)
    }

    final override fun onceVisible(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_VISIBLE, key, action)
    }

    final override fun doOnHidden(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_VISIBLE, callback)
    }

    // ----- FOCUSED STAGE ACTIONS -----

    final override fun doOnFocused(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_FOCUSED, callback)
    }

    final override fun onceFocused(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_FOCUSED, key, action)
    }

    final override fun doOnUnfocused(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_FOCUSED, callback)
    }

}