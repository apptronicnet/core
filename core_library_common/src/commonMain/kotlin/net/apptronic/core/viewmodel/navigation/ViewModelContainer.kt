package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.base.SubscriptionHolders
import net.apptronic.core.base.addTo
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.platform.debugError
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.distinctUntilChanged
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.and
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModelLifecycle

internal class ViewModelContainer(
        private val viewModel: IViewModel,
        private val parent: IViewModel,
        private val shouldShow: Entity<Boolean>
) {

    private val subscriptionHolders = SubscriptionHolders()

    private val attachedLocal = viewModel.value(false)
    private val boundLocal = viewModel.value(false)
    private val visibleLocal = viewModel.value(false)
    private val focusedLocal = viewModel.value(false)

    private val attachedParent = from(parent.observeAttached())
    private val boundParent = from(parent.observeBound())
    private val visibleParent = from(parent.observeVisible())
    private val focusedParent = from(parent.observeFocused())

    private val isAttached = (attachedLocal and attachedParent).distinctUntilChanged()
    private val isBound = (isAttached and boundLocal and boundParent).distinctUntilChanged()
    private val isVisible = (isBound and visibleLocal and visibleParent).distinctUntilChanged()
    private val isFocused = (isVisible and focusedLocal and focusedParent).distinctUntilChanged()
    private var shouldShowValue = true

    private fun from(parent: Observable<Boolean>): Entity<Boolean> {
        val property = viewModel.value<Boolean>().apply { set(false) }
        parent.subscribe {
            property.set(it)
        }.addTo(subscriptionHolders)
        return property
    }

    fun setAttached(value: Boolean) {
        if (value == attachedLocal.get()) {
            debugError(Error("$viewModel is already attached=$value"))
        }
        attachedLocal.set(value)
    }

    fun setBound(value: Boolean) {
        if (attachedLocal.get().not()) {
            debugError(Error("$viewModel is not attached"))
        }
        if (value == boundLocal.get()) {
            debugError(Error("$viewModel is already bound=$value"))
        }
        boundLocal.set(value)
    }

    fun setVisible(value: Boolean) {
        if (attachedLocal.get().not()) {
            debugError(Error("$viewModel is not bound"))
        }
        if (value == visibleLocal.get()) {
            debugError(Error("$viewModel is already visible=$value"))
        }
        visibleLocal.set(value)
    }

    fun setFocused(value: Boolean) {
        if (attachedLocal.get().not()) {
            debugError(Error("$viewModel is not visible"))
        }
        if (value == focusedLocal.get()) {
            debugError(Error("$viewModel is already focused=$value"))
        }
        focusedLocal.set(value)
    }

    private var visibilityChangeCallback: (Boolean) -> Unit = {}

    fun observeVisibilityChanged(callback: (Boolean) -> Unit) {
        visibilityChangeCallback = callback
    }

    init {
        bindStage(viewModel, ViewModelLifecycle.Attached, isAttached)
        bindStage(viewModel, ViewModelLifecycle.Bound, isBound)
        bindStage(viewModel, ViewModelLifecycle.Visible, isVisible)
        bindStage(viewModel, ViewModelLifecycle.Focused, isFocused)
        shouldShow.subscribe(viewModel.context) {
            if (shouldShowValue != it) {
                shouldShowValue = it
                visibilityChangeCallback.invoke(it)
            }
        }
    }

    private fun bindStage(viewModel: IViewModel, definition: LifecycleStageDefinition, entity: Entity<Boolean>) {
        entity.distinctUntilChanged().subscribe {
            if (it) {
                enterStage(viewModel.context, definition)
            } else {
                exitStage(viewModel.context, definition)
            }
        }
    }

    fun terminate() {
        subscriptionHolders.unsubscribe()
        viewModel.context.lifecycle.terminate()
    }

    fun shouldShow(): Boolean {
        return shouldShowValue
    }

    fun getViewModel(): IViewModel {
        return viewModel
    }

    override fun toString(): String {
        return "ViewModel=$viewModel stage=${viewModel.context.lifecycle.getActiveStage()?.getStageName()} shouldShowValue=$shouldShowValue"
    }

}