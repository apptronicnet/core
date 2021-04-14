package net.apptronic.core.viewmodel

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.doAsync
import net.apptronic.core.context.lifecycle.LifecycleStage
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.behavior.filterEquals
import net.apptronic.core.entity.bindContext
import net.apptronic.core.entity.commons.asEvent
import net.apptronic.core.entity.function.map

private val ViewModelAttachDescriptor = extensionDescriptor<ViewModel>()

interface IViewModel : IComponent {

    override val context: Context

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

    /**
     * Do same as [closeSelf] but make call asynchronously
     */
    fun closeSelfAsync(transitionInfo: Any? = null): Boolean

    // ----- CREATED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.Attached]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnAttach(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.Attached]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceAttached(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.Attached]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnDetach(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- BOUND STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.Bound]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnBind(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.Bound]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceBound(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.Bound]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnbind(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- VISIBLE STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.Visible]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnVisible(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.Visible]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceVisible(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.Visible]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnHidden(callback: LifecycleStage.OnExitHandler.() -> Unit)

    // ----- FOCUSED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.Focused]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnFocused(callback: LifecycleStage.OnEnterHandler.() -> Unit)

    /**
     * Subscribe to enter [ViewModelLifecycle.Focused]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceFocused(key: String = "", action: () -> Unit)

    /**
     * Subscribe to exit from [ViewModelLifecycle.Focused]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnUnfocused(callback: LifecycleStage.OnExitHandler.() -> Unit)

}

open class ViewModel : Component, IViewModel {

    private val isAttached = BehaviorSubject<Boolean>()
    private val isBound = BehaviorSubject<Boolean>()
    private val isVisible = BehaviorSubject<Boolean>()
    private val isFocused = BehaviorSubject<Boolean>()

    private var parent: ViewModelParent? = null

    constructor(context: Context) : super(context, ViewModelLifecycleDefinition) {
        stateOfStage(isAttached, ViewModelLifecycle.Attached)
        stateOfStage(isBound, ViewModelLifecycle.Bound)
        stateOfStage(isVisible, ViewModelLifecycle.Visible)
        stateOfStage(isFocused, ViewModelLifecycle.Focused)
    }

    constructor(parent: IViewModel) : super(parent.context, ViewModelLifecycleDefinition) {
        parent.observeAttached().subscribe(isAttached)
        parent.observeBound().subscribe(isBound)
        parent.observeVisible().subscribe(isVisible)
        parent.observeFocused().subscribe(isFocused)
    }

    private fun stateOfStage(
        target: BehaviorSubject<Boolean>,
        definition: LifecycleStageDefinition
    ): Observable<Boolean> {
        onEnterStage(definition) {
            target.update(true)
        }
        onExitStage(definition) {
            target.update(false)
        }
        return target
    }

    final override fun onAttachToParent(parent: ViewModelParent) {
        context.extensions[ViewModelAttachDescriptor]?.let {
            throw IllegalArgumentException(
                "Context of $this already have attached another [ViewModel]: $it. " +
                        "It is not allowed to attach multiple [ViewModel]s in one context " +
                        "except direct creation of one [ViewModel] inside other " +
                        "without [Navigator] or other [ViewModelParent]"
            )
        }
        context.extensions[ViewModelAttachDescriptor] = this
        context.lifecycle.enterStage(ViewModelLifecycle.Attached)
        context.plugins.nextViewModelAttached(this)
        this.parent = parent
    }

    final override fun onDetachFromParent() {
        context.lifecycle.exitStage(ViewModelLifecycle.Attached)
        this.parent = null
    }

    final override fun isAttached() = context.lifecycle.isStageEntered(ViewModelLifecycle.Attached)

    final override fun observeAttached(): Observable<Boolean> = isAttached

    final override fun isBound() = context.lifecycle.isStageEntered(ViewModelLifecycle.Bound)

    final override fun observeBound(): Observable<Boolean> = isBound

    final override fun isVisible() = context.lifecycle.isStageEntered(ViewModelLifecycle.Visible)

    final override fun observeVisible(): Observable<Boolean> = isVisible

    final override fun isFocused() = context.lifecycle.isStageEntered(ViewModelLifecycle.Focused)

    final override fun observeFocused(): Observable<Boolean> = isFocused

    private fun whenEntered(observable: Observable<Boolean>): Entity<Unit> {
        return observable.bindContext(context)
            .filterEquals(true)
            .map { Unit }
            .asEvent()
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

    override fun closeSelfAsync(transitionInfo: Any?): Boolean {
        return parent?.let {
            doAsync {
                it.requestCloseSelf(this@ViewModel, transitionInfo)
            }
            true
        } ?: false
    }

    final override fun doOnAttach(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.Attached, callback)
    }

    final override fun onceAttached(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.Attached, key, action)
    }

    final override fun doOnDetach(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.Attached, callback)
    }

    // ----- BOUND STAGE ACTIONS -----

    final override fun doOnBind(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.Bound, callback)
    }

    final override fun onceBound(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.Bound, key, action)
    }

    final override fun doOnUnbind(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.Bound, callback)
    }

    // ----- VISIBLE STAGE ACTIONS -----

    final override fun doOnVisible(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.Visible, callback)
    }

    final override fun onceVisible(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.Visible, key, action)
    }

    final override fun doOnHidden(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.Visible, callback)
    }

    // ----- FOCUSED STAGE ACTIONS -----

    final override fun doOnFocused(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.Focused, callback)
    }

    final override fun onceFocused(key: String, action: () -> Unit) {
        onceStage(ViewModelLifecycle.Focused, key, action)
    }

    final override fun doOnUnfocused(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.Focused, callback)
    }

}