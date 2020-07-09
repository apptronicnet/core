package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.SubscriptionHolders
import net.apptronic.core.base.addTo
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.distinctUntilChanged
import net.apptronic.core.component.entity.functions.and
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.component.value
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

internal class ViewModelContainer(
        private val viewModel: ViewModel,
        private val parent: ViewModel,
        private val shouldShow: Entity<Boolean>
) {

    private val subscriptionHolders = SubscriptionHolders()

    private val createdLocal = viewModel.value(false)
    private val boundLocal = viewModel.value(false)
    private val visibleLocal = viewModel.value(false)
    private val focusedLocal = viewModel.value(false)

    private val createdParent = from(parent.observeCreated())
    private val boundParent = from(parent.observeBound())
    private val visibleParent = from(parent.observeVisible())
    private val focusedParent = from(parent.observeFocused())

    private val isCreated = (createdLocal and createdParent).distinctUntilChanged()
    private val isBound = (isCreated and boundLocal and boundParent).distinctUntilChanged()
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

    fun setCreated(value: Boolean) {
        if (value == createdLocal.get()) {
            debugError(Error("$viewModel is already created=$value"))
        }
        createdLocal.set(value)
    }

    fun setBound(value: Boolean) {
        if (createdLocal.get().not()) {
            debugError(Error("$viewModel is not created"))
        }
        if (value == boundLocal.get()) {
            debugError(Error("$viewModel is already bound=$value"))
        }
        boundLocal.set(value)
    }

    fun setVisible(value: Boolean) {
        if (createdLocal.get().not()) {
            debugError(Error("$viewModel is not bound"))
        }
        if (value == visibleLocal.get()) {
            debugError(Error("$viewModel is already visible=$value"))
        }
        visibleLocal.set(value)
    }

    fun setFocused(value: Boolean) {
        if (createdLocal.get().not()) {
            debugError(Error("$viewModel is not visible"))
        }
        if (value == focusedLocal.get()) {
            debugError(Error("$viewModel is already focused=$value"))
        }
        focusedLocal.set(value)
    }

    private var visibilityChangeCallback: () -> Unit = {}

    fun observeVisibilityChanged(callback: () -> Unit) {
        visibilityChangeCallback = callback
    }

    init {
        bindStage(viewModel, ViewModelLifecycle.STAGE_CREATED, isCreated)
        bindStage(viewModel, ViewModelLifecycle.STAGE_BOUND, isBound)
        bindStage(viewModel, ViewModelLifecycle.STAGE_VISIBLE, isVisible)
        bindStage(viewModel, ViewModelLifecycle.STAGE_FOCUSED, isFocused)
        shouldShow.subscribe(viewModel.context) {
            if (shouldShowValue != it) {
                shouldShowValue = it
                visibilityChangeCallback.invoke()
            }
        }
    }

    private fun bindStage(viewModel: ViewModel, definition: LifecycleStageDefinition, entity: Entity<Boolean>) {
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

    fun getViewModel(): ViewModel {
        return viewModel
    }

    override fun toString(): String {
        return "ViewModel=$viewModel stage=${viewModel.context.lifecycle.getActiveStage()?.getStageName()} shouldShowValue=$shouldShowValue"
    }

}