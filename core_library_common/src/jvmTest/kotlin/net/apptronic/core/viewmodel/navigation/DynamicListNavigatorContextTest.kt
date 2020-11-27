package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.*
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class DynamicListNavigatorContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: ViewModelContext, builder: ViewModelAdapter<Any, Any, IViewModel>) : ViewModel(context) {

        val items = value<List<Any>>(emptyList())
        val navigator = listDynamicNavigator(items, builder)

    }

    class ChildModelNavigatorContext(context: ViewModelContext, builder: ViewModelAdapter<Any, Any, IViewModel>) : ViewModel(context) {

        val items = value<List<Any>>(emptyList())
        val navigatorContext = childContext()
        val navigator = listDynamicNavigator(items, builder, navigatorContext)

    }

    @Test
    fun shouldCreateNavigatorWithNavigatorcontext() {
        rootModel.listDynamicNavigator(viewModelAdapter { })
        rootModel.listDynamicNavigator(viewModelAdapter { }, rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext1() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listDynamicNavigator(viewModelAdapter { }, rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listDynamicNavigator(viewModelAdapter { }, rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModel(childModelContext,
                object : ViewModelAdapter<Int, Int, IViewModel> {
                    override fun getItemId(item: Int): Int = item
                    override fun createViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(rootModel.context.viewModelContext())
                    }
                }.asComposite()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelAt(0)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext,
                object : ViewModelAdapter<Int, Int, IViewModel> {
                    override fun getItemId(item: Int): Int = item
                    override fun createViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(rootModel.context.viewModelContext())
                    }
                }.asComposite()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelAt(0)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext,
                object : ViewModelAdapter<Int, Int, IViewModel> {
                    override fun getItemId(item: Int): Int = item
                    override fun createViewModel(parent: Context, item: Int): IViewModel {
                        return ViewModel(childModelContext.viewModelContext())
                    }
                }.asComposite()
        )
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.items.set(listOf(1))
        childModel.navigator.getViewModelAt(0)
    }

}