package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.container.ViewModelListNavigator

class ListScreenViewModel(context: ViewModelContext) : ViewModel(context), ListController {

    val listNavigator = ViewModelListNavigator(this)
    private val itemNames = mutableValue(mutableListOf<String>())
    val title = itemNames.map {
        it.joinToString(separator = " ")
    }

    init {
        getProvider().addInstance(ListControllerDescriptor, this)
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
            val newItem = getProvider().inject(ListItemTextViewModelDescriptor)
            it.add(0, newItem)
        }
    }

    fun onClickAddTextToMiddle() {
        listNavigator.update {
            val newItem = getProvider().inject(ListItemTextViewModelDescriptor)
            it.add(it.size / 2, newItem)
        }
    }

    fun onClickAddTextToEnd() {
        listNavigator.update {
            val newItem = getProvider().inject(ListItemTextViewModelDescriptor)
            it.add(it.size, newItem)
        }
    }

    fun onClickAddImageToStart() {
        listNavigator.update {
            val newItem = getProvider().inject(ListItemImageViewModelDescriptor)
            it.add(0, newItem)
        }
    }

    fun onClickAddImageToMiddle() {
        listNavigator.update {
            val newItem = getProvider().inject(ListItemImageViewModelDescriptor)
            it.add(it.size / 2, newItem)
        }
    }

    fun onClickAddImageToEnd() {
        listNavigator.update {
            val newItem = getProvider().inject(ListItemImageViewModelDescriptor)
            it.add(it.size, newItem)
        }
    }

}