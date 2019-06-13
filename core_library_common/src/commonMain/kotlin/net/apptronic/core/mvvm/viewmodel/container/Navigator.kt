package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.entities.ComponentEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent
import net.apptronic.core.threading.WorkerDefinition

abstract class Navigator<T>(viewModel: ViewModel) : ComponentEntity<T>(viewModel),
    EntityValue<T>, ViewModelParent {

    protected val uiWorker = viewModel.getScheduler().getWorker(WorkerDefinition.DEFAULT)
    protected val uiAsyncWorker = viewModel.getScheduler().getWorker(WorkerDefinition.DEFAULT_ASYNC)

    override fun getValueHolder(): ValueHolder<T>? {
        return ValueHolder(get())
    }

    override fun isSet(): Boolean {
        return true
    }

    override fun doIfSet(action: (T) -> Unit): Boolean {
        action.invoke(get())
        return true
    }

}