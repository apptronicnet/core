package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.entities.asProperty
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.platformLogError

@UnderDevelopment
fun IViewModel.stackNavigationModel(navigatorContext: Context = this.context): StackNavigationModel {
    context.verifyNavigatorContext(navigatorContext)
    return StackNavigationModel(this, navigatorContext)
}

/**
 * This model allows to implement stack navigation using [StaticListNavigator]. It allows to handle user gestures to perform
 * back navigation and save views for items in back stack improving rendering performance by preventing destroying
 * and recreating views for back stack.
 *
 * Based on adapter implementation there may be no possibility to have interpretation for transitionInfo events.
 */
@UnderDevelopment
class StackNavigationModel internal constructor(
        parent: IViewModel,
        override val navigatorContext: Context
) : Navigator<StackNavigatorContent>(parent), IStackNavigationModel, SingleViewModelNavigationModel, SingleViewModelListNavigationModel {

    private val listNavigator = StatelessStaticListNavigator(parent, navigatorContext)

    private fun updateInternal(transitionInfo: Any?, stackTransition: StackTransition, action: (MutableList<IViewModel>) -> Unit) {
        val current = getStack()
        val next = current.toTypedArray().toMutableList()
        action(next)
        updateInternal(transitionInfo, stackTransition, next)
    }

    private fun updateInternal(transitionInfo: Any?, stackTransition: StackTransition, next: List<IViewModel>) {
        val current = getStack()
        next.forEach {
            it.verifyContext()
        }
        val isNewOnFront = when (stackTransition) {
            StackTransition.Auto -> next.size >= current.size
            StackTransition.NewOnFront -> true
            StackTransition.NewOnBack -> false
        }
        listNavigator.set(next, TransitionInfo(isNewOnFront, transitionInfo))
    }

    /**
     * Notify adapter that user is navigated to specific [index] of stack.
     *
     * This will clear stack after [index].
     */
    fun onNavigated(index: Int) {
        val next = getStack().take(index + 1)
        updateInternal(null, StackTransition.Auto, next)
    }

    override fun setAdapter(adapter: SingleViewModelAdapter) {
        listNavigator.setAdapter(SelectorAdapter(adapter))
    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : ViewModelListAdapter<Unit> {

        var oldViewModel: ViewModelItem? = null
        var targetInitialized: Boolean = false

        override fun onDataChanged(items: List<ViewModelItem>, state: Unit, changeInfo: Any?) {
            val oldViewModelItem: ViewModelItem? = this.oldViewModel
            val newViewModelItem: ViewModelItem? = items.lastOrNull()
            if (oldViewModelItem != newViewModelItem || !targetInitialized) {
                targetInitialized = true
                oldViewModelItem?.setFocused(false)
                oldViewModelItem?.setVisible(false)
                oldViewModelItem?.setBound(false)
                newViewModelItem?.setBound(true)
                val transitionInfo = if (changeInfo == null) {
                    TransitionInfo(true, null)
                } else {
                    changeInfo as? TransitionInfo ?: kotlin.run {
                        platformLogError(Error("StackNavigationModel.SelectorAdapter.onDataChanged: changeInfo is not instance of TransitionInfo!!!"))
                        TransitionInfo(true, null)
                    }
                }
                target.onInvalidate(newViewModelItem, transitionInfo)
                newViewModelItem?.setVisible(true)
                newViewModelItem?.setFocused(true)
                this.oldViewModel = newViewModelItem
            }
        }

    }

    override fun setAdapter(adapter: SingleViewModelListAdapter) {
        listNavigator.setAdapter(SelectorListAdapter(adapter))
    }

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : ViewModelListAdapter<Unit> {

        override fun onDataChanged(items: List<ViewModelItem>, state: Unit, changeInfo: Any?) {
            val transitionInfo = if (changeInfo == null) {
                TransitionInfo(true, null)
            } else {
                changeInfo as? TransitionInfo ?: kotlin.run {
                    platformLogError(Error("StackNavigationModel.SelectorListAdapter.onDataChanged: changeInfo is not instance of TransitionInfo!!!"))
                    TransitionInfo(true, null)
                }
            }
            target.onInvalidate(listNavigator.getViewModelItems(), items.lastIndex, transitionInfo)
        }

    }

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        listNavigator.requestCloseSelf(viewModel, transitionInfo)
    }

    override val content: EntityValue<StackNavigatorContent> = listNavigator.content.map {
        StackNavigatorContent(
                isInProgress = false,
                actualModel = it.all.lastOrNull(),
                visibleModel = it.visible.lastOrNull(),
                size = it.countAll,
                stack = it.all)
    }.asProperty()

    fun currentViewModel(): IViewModel? {
        return getStack().lastOrNull()
    }

    override fun replaceStack(newStack: List<IViewModel>, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition, newStack)
    }

    override fun add(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition) {
            it.add(viewModel)
        }
    }

    override fun remove(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition) {
            it.remove(viewModel)
        }
    }

    override fun clear(transitionInfo: Any?) {
        updateInternal(transitionInfo, StackTransition.Auto, emptyList())
    }

    override fun getItemAt(index: Int): IViewModel {
        return getStack()[index]
    }

    override fun getSize(): Int {
        return getStack().size
    }

    override fun getStack(): List<IViewModel> {
        return mutableListOf<IViewModel>().apply {
            addAll(listNavigator.getAll())
        }
    }

    override fun popBackStackTo(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition): Boolean {
        val previous = getStack().toTypedArray().toMutableList()
        val index = previous.indexOf(viewModel)
        if (index < 0) {
            return false
        }
        val next = previous.subList(0, index + 1)
        updateInternal(transitionInfo, stackTransition, next)
        return previous.size != next.size
    }

    override fun replace(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        viewModel.verifyContext()
        updateInternal(transitionInfo, stackTransition) {
            if (it.size > 0) {
                it.removeAt(it.size - 1)
            }
            it.add(viewModel)
        }
    }

    override fun replaceAll(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition, listOf(viewModel))
    }

}