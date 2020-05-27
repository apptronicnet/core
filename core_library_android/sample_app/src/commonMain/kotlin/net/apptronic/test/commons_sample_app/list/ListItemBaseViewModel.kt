package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ListItemBaseViewModel(
    private val indexNumber: Int, parent: Context
) : ViewModel(parent, EmptyViewModelContext) {

    private val controller = inject(ListControllerDescriptor)

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