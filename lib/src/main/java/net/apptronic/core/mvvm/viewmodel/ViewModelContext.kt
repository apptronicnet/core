package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.base.ComponentLoggerDescriptor
import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage

open class ViewModelContext(parent: ComponentContext) : SubContext(parent) {

    private val logger = parent.objects().get(ComponentLoggerDescriptor)

    private val lifecycle = ViewModelLifecycle(workers())

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    fun initLifecycleLogging(viewModel: ViewModel) {
        lifecycle.getStage(Lifecycle.ROOT_STAGE)?.doOnce {
            logger.log("ViewModelLifecycle: $viewModel initialized")
        }
        subscribeStage(viewModel, lifecycle.created)
        subscribeStage(viewModel, lifecycle.bound)
        subscribeStage(viewModel, lifecycle.visible)
        subscribeStage(viewModel, lifecycle.focused)
        lifecycle.getStage(Lifecycle.ROOT_STAGE)?.doOnExit {
            logger.log("ViewModelLifecycle: $viewModel terminated")
        }
    }

    private fun subscribeStage(viewModel: ViewModel, stage: LifecycleStage) {
        val stageName = stage.getStageName()
        stage.doOnEnter {
            logger.log("ViewModelLifecycle: $viewModel entered stage$stageName")
        }
        stage.doOnExit {
            logger.log("ViewModelLifecycle: $viewModel exited stage$stageName")
        }
    }

}