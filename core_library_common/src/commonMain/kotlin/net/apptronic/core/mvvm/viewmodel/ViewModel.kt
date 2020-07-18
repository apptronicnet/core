package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.Component
import net.apptronic.core.component.applyPlugins
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.doWhen
import net.apptronic.core.component.entity.behavior.setup
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.mvvm.viewmodel.navigation.*

open class ViewModel : Component {

    final override val context: ViewModelContext

    private val isAttached = BehaviorSubject<Boolean>()
    private val isBound = BehaviorSubject<Boolean>()
    private val isVisible = BehaviorSubject<Boolean>()
    private val isFocused = BehaviorSubject<Boolean>()

    constructor(context: ViewModelContext) {
        this.context = context
        doInit()
    }

    constructor(parent: ViewModel) {
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
        applyPlugins()
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

    fun stackNavigator(): StackNavigator {
        return StackNavigator(this)
    }

    fun stackNavigator(source: Entity<ViewModel>): StackNavigator {
        return StackNavigator(this).setup {
            source.subscribe(context) {
                set(it)
            }
        }
    }

    fun <T, Id, VM : ViewModel> listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
        return ViewModelListBuilder(this, builder)
    }

    fun listNavigator(): ListNavigator {
        return ListNavigator(this)
    }

    fun <T, Id, VM : ViewModel> listNavigator(
            source: Entity<out List<T>>,
            builder: ViewModelBuilder<T, Id, VM>
    ): ListNavigator {
        val listBuilder = listBuilder(builder)
        listBuilder.updateFrom(source)
        return listNavigator().setAs(listBuilder)
    }

    fun <T : Any, Id: Any, VM : ViewModel> listDynamicNavigator(
            builder: ViewModelBuilder<T, Id, VM>
    ): DynamicListNavigator<T, Id, VM> {
        return DynamicListNavigator(this, builder)
    }

    fun <T : Any, Id: Any, VM : ViewModel> listDynamicNavigator(
            source: Entity<List<T>>,
            builder: ViewModelBuilder<T, Id, VM>
    ): DynamicListNavigator<T, Id, VM> {
        val navigator = listDynamicNavigator(builder)
        source.subscribe(context) {
            navigator.set(it)
        }
        return navigator
    }

    fun listNavigator(source: Entity<List<ViewModel>>): ListNavigator {
        return ListNavigator(this).setup {
            source.subscribe(context) {
                set(it)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    internal fun onAttachedToContainer(parent: ViewModelParent) {
        context.lifecycle.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
    }

    @Suppress("UNUSED_PARAMETER")
    internal fun onDetachedFromContainer(parent: ViewModelParent) {
        context.lifecycle.exitStage(ViewModelLifecycle.STAGE_ATTACHED)
    }

    private var parent: ViewModelParent? = null

    fun onAttachToParent(parent: ViewModelParent) {
        this.parent = parent
    }

    fun onDetachFromParent() {
        this.parent = null
    }

    fun isAttached() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_ATTACHED)

    fun observeAttached(): Observable<Boolean> = isAttached

    fun isBound() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_BOUND)

    fun observeBound(): Observable<Boolean> = isBound

    fun isVisible() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_VISIBLE)

    fun observeVisible(): Observable<Boolean> = isVisible

    fun isFocused() = context.lifecycle.isStageEntered(ViewModelLifecycle.STAGE_FOCUSED)

    fun observeFocused(): Observable<Boolean> = isFocused

    private fun whenEntered(observable: Observable<Boolean>): Entity<Unit> {
        return genericEvent().also { event ->
            doWhen(observable.bindContext(context)) {
                event.sendEvent()
            }
        }
    }

    /**
     * Create [Entity] which emits item when [ViewModel] entering attached stage
     */
    fun whenAttached(): Entity<Unit> {
        return whenEntered(isAttached)
    }

    /**
     * Create [Entity] which emits item when [ViewModel] entering bound stage
     */
    fun whenBound(): Entity<Unit> {
        return whenEntered(isBound)
    }

    /**
     * Create [Entity] which emits item when [ViewModel] entering visible stage
     */
    fun whenVisible(): Entity<Unit> {
        return whenEntered(isVisible)
    }

    /**
     * Create [Entity] which emits item when [ViewModel] entering focused stage
     */
    fun whenFocused(): Entity<Unit> {
        return whenEntered(isFocused)
    }

    /**
     * Check is current state is exactly on state attached
     */
    fun isStateAttached(): Boolean {
        return isAttached() && !isBound()
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
        return context.lifecycle.isTerminated()
    }

    /**
     * Request [ViewModelParent] ti close this [ViewModel]
     * @return true is parent exists and false otherwise
     */
    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            it.requestCloseSelf(this, transitionInfo)
            true
        } ?: false
    }

    // ----- CREATED STAGE ACTIONS -----

    /**
     * Subscribe to enter to [ViewModelLifecycle.STAGE_ATTACHED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnAttach(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(ViewModelLifecycle.STAGE_ATTACHED, callback)
    }

    /**
     * Subscribe to enter [ViewModelLifecycle.STAGE_ATTACHED]. Called one time.
     * @param key unique key for action. If action with same key already subscribed it will be
     * replaced by new action and previous registered action will not be called.
     */
    fun onceAttached(key: String = "", action: () -> Unit) {
        onceStage(ViewModelLifecycle.STAGE_ATTACHED, key, action)
    }

    /**
     * Subscribe to exit from [ViewModelLifecycle.STAGE_ATTACHED]. Called each time when [ViewModel]
     * enter specified stage until [ViewModel] exit from current stage (from which this method called)
     */
    fun doOnDetach(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(ViewModelLifecycle.STAGE_ATTACHED, callback)
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