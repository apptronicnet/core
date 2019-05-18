package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.SubscriptionHolders
import net.apptronic.core.base.addTo
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.functions.variants.and
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

class ViewModelContainerItem(
    val viewModel: ViewModel,
    val parent: ViewModel
) {

    private val subscriptionHolders = SubscriptionHolders()

    private val createdLocal = Value<Boolean>(viewModel).apply { set(false) }
    private val boundLocal = Value<Boolean>(viewModel).apply { set(false) }
    private val visibleLocal = Value<Boolean>(viewModel).apply { set(false) }
    private val focusedLocal = Value<Boolean>(viewModel).apply { set(false) }

    private val createdParent = from(parent.observeCreated())
    private val boundParent = from(parent.observeBound())
    private val visibleParent = from(parent.observeVisible())
    private val focusedParent = from(parent.observeFocused())

    private val isCreated = createdLocal and createdParent
    private val isBound = boundLocal and boundParent
    private val isVisible = visibleLocal and visibleParent
    private val isFocused = focusedLocal and focusedParent

    private fun from(parent: Observable<Boolean>): Entity<Boolean> {
        val property = Value<Boolean>(viewModel).apply { set(false) }
        parent.subscribe {
            property.set(it)
        }.addTo(subscriptionHolders)
        return property
    }

    fun setCreated(value: Boolean) {
        createdLocal.set(value)
    }

    fun setBound(value: Boolean) {
        boundLocal.set(value)
    }

    fun setVisible(value: Boolean) {
        visibleLocal.set(value)
    }

    fun setFocused(value: Boolean) {
        focusedLocal.set(value)
    }

    init {
        bindStage(viewModel, ViewModelLifecycle.STAGE_CREATED, isCreated)
        bindStage(viewModel, ViewModelLifecycle.STAGE_BOUND, isBound)
        bindStage(viewModel, ViewModelLifecycle.STAGE_VISIBLE, isVisible)
        bindStage(viewModel, ViewModelLifecycle.STAGE_FOCUSED, isFocused)
        parent.doOnTerminate {
            terminate()
        }
    }

    private fun bindStage(viewModel: ViewModel, stageName: String, entity: Entity<Boolean>) {
        entity.subscribe {
            if (it) {
                enterStage(viewModel, stageName)
            } else {
                exitStage(viewModel, stageName)
            }
        }
    }

    fun terminate() {
        subscriptionHolders.unsubscribe()
        viewModel.terminate()
    }

}