package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.entity.behavior.whenever
import net.apptronic.core.component.entity.behavior.wheneverNot
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.and

class ViewModelRoot(
    val coreViewModel: ViewModel,
    context: ComponentContext
) : Component(context),
    ViewModelParent {

    private val isCreated = value(false)
    private val isVisible = value(false)
    private val isFocused = value(false)

    private val isCreatedStage = value<Boolean>().setAs(isCreated)
    private val isVisibleStage = value<Boolean>().setAs(
        isCreatedStage and isVisible
    )
    private val isFocusedStage = value<Boolean>().setAs(
        isVisibleStage and isFocused
    )

    private var onCloseListener: () -> Unit = {}

    init {
        whenever(isCreatedStage) {
            coreViewModel.getLifecycle().enterStage(ViewModelLifecycle.STAGE_CREATED)
        }
        wheneverNot(isCreatedStage) {
            coreViewModel.getLifecycle().exitStage(ViewModelLifecycle.STAGE_CREATED)
        }
        whenever(isVisibleStage) {
            coreViewModel.getLifecycle().enterStage(ViewModelLifecycle.STAGE_VISIBLE)
        }
        wheneverNot(isVisibleStage) {
            coreViewModel.getLifecycle().exitStage(ViewModelLifecycle.STAGE_VISIBLE)
        }
        whenever(isFocusedStage) {
            coreViewModel.getLifecycle().enterStage(ViewModelLifecycle.STAGE_FOCUSED)
        }
        wheneverNot(isFocusedStage) {
            coreViewModel.getLifecycle().exitStage(ViewModelLifecycle.STAGE_FOCUSED)
        }
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        onCloseListener.invoke()
    }

    fun setOnCloseListener(onCloseListener: () -> Unit) {
        this.onCloseListener = onCloseListener
    }

}