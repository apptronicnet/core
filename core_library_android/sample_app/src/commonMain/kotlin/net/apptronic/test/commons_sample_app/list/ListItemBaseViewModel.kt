package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel

abstract class ListItemBaseViewModel(context: Context, private val indexNumber: Int) :
    ViewModel(context) {

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