package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.TestViewModel
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class DynamicListNavigatorContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: ViewModelContext, builder: ViewModelFactory) : ViewModel(context) {

        val items = value<List<Any>>(emptyList())
        val navigator = listDynamicNavigator(items, builder)

    }

    class ChildModelNavigatorContext(context: ViewModelContext, builder: ViewModelFactory) : ViewModel(context) {

        val items = value<List<Any>>(emptyList())
        val navigatorContext = childContext()
        val navigator = listDynamicNavigator(items, builder, navigatorContext)

    }

    @Test
    fun shouldCreateNavigatorWithNavigatorcontext() {
        rootModel.listDynamicNavigator(viewModelFactory { })
        rootModel.listDynamicNavigator(viewModelFactory { }, rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext1() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listDynamicNavigator(viewModelFactory { }, rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listDynamicNavigator(viewModelFactory { }, rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModel(childModelContext,
                object : ViewModelBuilder<Int, Int, IViewModel> {
                    override fun getId(item: Int): Int = item
                    override fun onCreateViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(rootModel.context.viewModelContext())
                    }
                }.asFactory()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelItemAt(0)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext,
                object : ViewModelBuilder<Int, Int, IViewModel> {
                    override fun getId(item: Int): Int = item
                    override fun onCreateViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(rootModel.context.viewModelContext())
                    }
                }.asFactory()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelItemAt(0)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext,
                object : ViewModelBuilder<Int, Int, IViewModel> {
                    override fun getId(item: Int): Int = item
                    override fun onCreateViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(childModelContext.viewModelContext())
                    }
                }.asFactory()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelItemAt(0)
    }

}