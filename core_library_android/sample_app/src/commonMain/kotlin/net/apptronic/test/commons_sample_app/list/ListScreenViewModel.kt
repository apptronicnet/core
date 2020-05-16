package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.behavior.merge
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.inject
import net.apptronic.core.component.mutableValue
import net.apptronic.core.mvvm.viewmodel.ViewModel

class ListScreenViewModel(parent: Context) : ViewModel(parent, listContext), ListController {

    val listNavigator = listNavigator()
    private val itemNames = mutableValue(mutableListOf<String>())
    val title = merge(listNavigator, itemNames) { viewModels, names ->
        viewModels.filter { viewModel ->
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

    override fun onRemoveRequest(viewModel: ViewModel) {
        listNavigator.update {
            it.remove(viewModel)
        }
    }

    override fun onVisible(name: String) {
        itemNames.update {
            it.add(name)
        }
    }

    override fun onNotVisible(name: String) {
        itemNames.update {
            it.remove(name)
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