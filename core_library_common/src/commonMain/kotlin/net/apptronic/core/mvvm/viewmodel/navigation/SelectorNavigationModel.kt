package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Navigation model which manages list of [ViewModel]s and displays one single [ViewModel] at time from it
 */
@UnderDevelopment
interface SelectorNavigationModel : INavigator<IViewModel?> {

    companion object {
        /**
         * Defines that navigator should not show anything
         */
        const val SELECTOR_NOTHING = -1

        /**
         * Defines that selection index is not changed during any operation
         */
        const val SELECTOR_SAME_POSITION = -2

        /**
         * Defines that selection index is not changed during any operation
         */
        const val SELECTOR_SAME_ITEM = -3

        /**
         * Defines that selection index should be taken as last
         */
        const val SELECTOR_LAST = -4
    }

    val size: Int

    val lastIndex: Int
        get() {
            return size - 1
        }

    val list: List<IViewModel>

    val selectorIndex: Int

    fun setSelectorIndex(index: Int, transitionSpec: Any? = null)

    /**
     * Set navigator to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(viewModel: IViewModel?, show: Boolean = true) {
        if (viewModel != null) {
            val selectorIndex = if (show) 0 else SELECTOR_NOTHING
            replaceAll(viewModel, null, selectorIndex = selectorIndex)
        } else {
            clear()
        }
    }

    /**
     * Set navigator to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(list: List<IViewModel>, selectorIndex: Int = 0) {
        replaceList(list, null, selectorIndex)
    }

    /**
     * Remove all [ViewModel]s and display nothing
     */
    fun clear(transitionSpec: Any? = null) {
        replaceList(emptyList(), transitionSpec, SELECTOR_NOTHING)
    }

    /**
     * Replace all [ViewModel]s single [ViewModel] and show it
     */
    fun replaceAll(viewModel: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = 0) {
        replaceList(listOf(viewModel), transitionSpec, selectorIndex)
    }

    fun replaceAll(transitionSpec: Any? = null, selectorIndex: Int = 0, builder: Context.() -> IViewModel) {
        val viewModel = navigatorContext.builder()
        replaceAll(viewModel, transitionSpec, selectorIndex)
    }

    fun remove(viewModel: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        update(transitionSpec, selectorIndex) {
            it.remove(viewModel)
        }
    }

    fun removeAt(index: Int, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        if (index in 0 until size) {
            update(transitionSpec, selectorIndex) {
                it.removeAt(index)
            }
        }
    }

    fun replace(oldInstance: IViewModel, newInstance: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        update(transitionSpec, selectorIndex) {
            val index = it.indexOf(oldInstance)
            if (index >= 0) {
                it.removeAt(index)
                it.add(index, newInstance)
            }
        }
    }

    fun replace(oldInstance: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION, builder: Context.() -> IViewModel) {
        val newInstance = navigatorContext.builder()
        replace(oldInstance, newInstance, transitionSpec, selectorIndex)
    }

    fun replaceAt(index: Int, newInstance: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        if (index in 0 until size) {
            update(transitionSpec, selectorIndex) {
                if (index >= 0) {
                    it.removeAt(index)
                    it.add(index, newInstance)
                }
            }
        }
    }

    fun replaceAt(index: Int, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION, builder: Context.() -> IViewModel) {
        if (index in 0 until size) {
            val newInstance = navigatorContext.builder()
            replaceAt(index, newInstance, transitionSpec, selectorIndex)
        }
    }

    fun add(viewModel: IViewModel, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        update(transitionSpec, selectorIndex) {
            it.add(viewModel)
        }
    }

    fun add(transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION, builder: Context.() -> IViewModel) {
        val viewModel = navigatorContext.builder()
        add(viewModel, transitionSpec, selectorIndex)
    }

    fun replaceList(newList: List<IViewModel>, transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION) {
        update(transitionSpec, selectorIndex) {
            it.clear()
            it.addAll(newList)
        }
    }

    fun replaceList(transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_POSITION, builder: Context.() -> List<ViewModel>) {
        val newList = navigatorContext.builder()
        replaceList(newList, transitionSpec, selectorIndex)
    }

    fun update(transitionSpec: Any? = null, selectorIndex: Int = SELECTOR_SAME_ITEM, builder: Context.(MutableList<IViewModel>) -> Unit)

}