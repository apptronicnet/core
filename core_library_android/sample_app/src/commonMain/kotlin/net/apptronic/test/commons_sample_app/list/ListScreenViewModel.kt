package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.entity.function.map
import net.apptronic.core.entity.function.merge
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.listNavigator

fun Contextual.listScreenViewModel() = ListScreenViewModel(
    childContext {
        dependencyModule(ListModule)
    }
)

class ListScreenViewModel internal constructor(context: Context) : ViewModel(context),
    ListController {

    val listNavigator = listNavigator()
    private val itemNames = mutableValue(mutableListOf<String>())
    val title = merge(listNavigator.content, itemNames) { content, names ->
        content.all.filter { viewModel ->
            viewModel is ListItemBaseViewModel && names.contains(viewModel.getName())
        }.map {
            (it as ListItemBaseViewModel).getName()
        }
    }.map {
        it.joinToString(separator = " ")
    }

    init {
        context.dependencyDispatcher.addInstance(ListControllerDescriptor, this)
    }

    override fun onRemoveRequest(viewModel: IViewModel) {
        listNavigator.update {
            it.remove(viewModel)
        }
    }

    override fun onVisible(name: String) {
        itemNames.updateValue {
            it.add(name)
            it
        }
    }

    override fun onNotVisible(name: String) {
        itemNames.updateValue {
            it.remove(name)
            it
        }
    }

    fun onClickAddTextToStart() {
        listNavigator.update {
            val newItem = inject(ListItemTextViewModelDescriptor)
            it.add(0, newItem)
        }
    }

    fun onClickAddTextToMiddle() {
        listNavigator.update {
            val newItem = inject(ListItemTextViewModelDescriptor)
            it.add(it.size / 2, newItem)
        }
    }

    fun onClickAddTextToEnd() {
        listNavigator.update {
            val newItem = inject(ListItemTextViewModelDescriptor)
            it.add(it.size, newItem)
        }
    }

    fun onClickAddImageToStart() {
        listNavigator.update {
            val newItem = inject(ListItemImageViewModelDescriptor)
            it.add(0, newItem)
        }
    }

    fun onClickAddImageToMiddle() {
        listNavigator.update {
            val newItem = inject(ListItemImageViewModelDescriptor)
            it.add(it.size / 2, newItem)
        }
    }

    fun onClickAddImageToEnd() {
        listNavigator.update {
            val newItem = inject(ListItemImageViewModelDescriptor)
            it.add(it.size, newItem)
        }
    }

}