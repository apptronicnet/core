package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

abstract class ListItemBaseViewModel(index: Int, parent: Context) :
    ViewModel(ViewModelContext(parent)) {

    private val controller = getProvider().inject(ListControllerDescriptor)

    val index = value(index)

    fun onRemoveButtonClick() {
        controller.onRemoveRequest(this)
    }

    abstract fun getName(): String

    init {
        doOnVisible {
            controller.onVisible(getName())
            onExit {
                controller.onNotVisible(getName())
            }
        }
    }

}