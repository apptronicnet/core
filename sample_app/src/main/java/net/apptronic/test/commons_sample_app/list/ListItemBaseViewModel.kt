package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

abstract class ListItemBaseViewModel(
    private val indexNumber: Int, parent: Context
) :
    ViewModel(ViewModelContext(parent)) {

    private val controller = getProvider().inject(ListControllerDescriptor)

    val index = value(indexNumber)

    fun onRemoveButtonClick() {
        controller.onRemoveRequest(this)
    }

    abstract fun getName(): String

    init {
        doOnBind {
            controller.onVisible(getName())
            onExit {
                controller.onNotVisible(getName())
            }
        }
    }

    override fun toString(): String {
        return "ListItem-$indexNumber"
    }

}