package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.viewmodel.*
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class ListNavigatorContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = listNavigator()

    }

    class ChildModelNavigatorContext(context: ViewModelContext) : ViewModel(context) {

        val navigatorContext = childContext()
        val navigator = listNavigator(navigatorContext)

    }

    @Test
    fun shouldCreateNavigatorWithNavigatorcontext() {
        rootModel.listNavigator()
        rootModel.listNavigator(rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext1() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listNavigator(rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.listNavigator(rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModel(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.set(
                listOf(
                        ViewModel(rootModel.context.viewModelContext())
                )
        )
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.set(
                listOf(
                        ViewModel(rootModel.context.viewModelContext())
                )
        )
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.set(
                listOf(
                        ViewModel(childModelContext.viewModelContext())
                )
        )
    }

}