package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Navigation model which manages list of [ViewModel]s and displays one single [ViewModel] at time from it
 */
@UnderDevelopment
interface SelectorNavigationModel : INavigator {

    companion object {
        /**
         * Defines that navigator should not show anything
         */
        const val SELECTOR_NOTHING = -1

        /**
         * Defines that selection index is not changed during any operation
         */
        const val SELECTOR_NOT_CHANGED = -2

        /**
         * Defines that selection index should be taken as last
         */
        const val SELECTOR_LAST = -3
    }

    val size: Int

    val lastIndex: Int
        get() {
            return size - 1
        }

    val list: List<IViewModel>

    val selectorIndex: Int

    fun setSelectorIndex(index: Int, transitionInfo: Any? = null)

    /**
     * Set navigator to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(viewModel: IViewModel?, selectorIndex: Int = 0) {
        if (viewModel != null) {
            replaceAll(viewModel, null)
        } else {
            clear()
        }
    }

    /**
     * Remove all [ViewModel]s and display nothing
     */
    fun clear(transitionInfo: Any? = null) {
        replaceList(emptyList(), transitionInfo, SELECTOR_NOTHING)
    }

    /**
     * Replace all [ViewModel]s single [ViewModel] and show it
     */
    fun replaceAll(viewModel: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = 0) {
        replaceList(listOf(viewModel), transitionInfo, selectorIndex)
    }

    fun replaceAll(transitionInfo: Any? = null, selectorIndex: Int = 0, builder: Context.() -> IViewModel) {
        val viewModel = navigatorContext.builder()
        replaceAll(viewModel, transitionInfo, selectorIndex)
    }

    fun remove(viewModel: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        update(transitionInfo, selectorIndex) {
            it.remove(viewModel)
        }
    }

    fun removeAt(index: Int, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        if (index in 0 until size) {
            update(transitionInfo, selectorIndex) {
                it.removeAt(index)
            }
        }
    }

    fun replace(oldInstance: IViewModel, newInstance: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        update(transitionInfo, selectorIndex) {
            val index = it.indexOf(oldInstance)
            if (index >= 0) {
                it.removeAt(index)
                it.add(index, newInstance)
            }
        }
    }

    fun replace(oldInstance: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED, builder: Context.() -> IViewModel) {
        val newInstance = navigatorContext.builder()
        replace(oldInstance, newInstance, transitionInfo, selectorIndex)
    }

    fun replaceAt(index: Int, newInstance: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        if (index in 0 until size) {
            update(transitionInfo, selectorIndex) {
                if (index >= 0) {
                    it.removeAt(index)
                    it.add(index, newInstance)
                }
            }
        }
    }

    fun replaceAt(index: Int, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED, builder: Context.() -> IViewModel) {
        if (index in 0 until size) {
            val newInstance = navigatorContext.builder()
            replaceAt(index, newInstance, transitionInfo, selectorIndex)
        }
    }

    fun add(viewModel: IViewModel, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        update(transitionInfo, selectorIndex) {
            it.add(viewModel)
        }
    }

    fun add(transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED, builder: Context.() -> IViewModel) {
        val viewModel = navigatorContext.builder()
        add(viewModel, transitionInfo, selectorIndex)
    }

    fun replaceList(newList: List<IViewModel>, transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED) {
        update(transitionInfo, selectorIndex) {
            it.clear()
            it.addAll(newList)
        }
    }

    fun replaceList(transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED, builder: Context.() -> List<ViewModel>) {
        val newList = navigatorContext.builder()
        replaceList(newList, transitionInfo, selectorIndex)
    }

    fun update(transitionInfo: Any? = null, selectorIndex: Int = SELECTOR_NOT_CHANGED, builder: Context.(MutableList<IViewModel>) -> Unit)

}