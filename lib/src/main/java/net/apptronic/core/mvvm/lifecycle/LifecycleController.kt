package net.apptronic.core.mvvm.lifecycle

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.and
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

class LifecycleController(
    private val parentContext: Context,
    private val parent: LifecycleController?
) {

    private val createdLocal = Value<Boolean>(parentContext).apply { set(false) }
    private val boundLocal = Value<Boolean>(parentContext).apply { set(false) }
    private val visibleLocal = Value<Boolean>(parentContext).apply { set(false) }
    private val focusedLocal = Value<Boolean>(parentContext).apply { set(false) }

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

    private fun isCreated(): Predicate<Boolean> {
        return createdLocal
    }

    private fun isBound(): Predicate<Boolean> {
        return isCreated() and boundLocal
    }

    private fun isVisible(): Predicate<Boolean> {
        return isBound() and visibleLocal
    }

    private fun isFocused(): Predicate<Boolean> {
        return isVisible() and focusedLocal
    }

    private val isCreated = Value<Boolean>(parentContext)
    private val isBound = Value<Boolean>(parentContext)
    private val isVisible = Value<Boolean>(parentContext)
    private val isFocused = Value<Boolean>(parentContext)

    init {
        if (parent != null) {
            isCreated.setAs(isCreated() and parent.isCreated())
            isBound.setAs(isBound() and parent.isBound())
            isVisible.setAs(isVisible() and parent.isVisible())
            isFocused.setAs(isFocused() and parent.isFocused())
        } else {
            isCreated.setAs(isCreated())
            isBound.setAs(isBound())
            isVisible.setAs(isVisible())
            isFocused.setAs(isFocused())
        }
    }

    fun bindTarget(viewModel: ViewModel) {
        bindStage(viewModel, ViewModelLifecycle.STAGE_CREATED, isCreated)
        bindStage(viewModel, ViewModelLifecycle.STAGE_BOUND, isBound)
        bindStage(viewModel, ViewModelLifecycle.STAGE_VISIBLE, isVisible)
        bindStage(viewModel, ViewModelLifecycle.STAGE_FOCUSED, isFocused)
        parentContext.getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnExit {
            viewModel.terminate()
        }
    }

    private fun bindStage(viewModel: ViewModel, stageName: String, predicate: Predicate<Boolean>) {
        predicate.subscribe {
            if (it) {
                enterStage(viewModel, stageName)
            } else {
                exitStage(viewModel, stageName)
            }
        }
    }

}