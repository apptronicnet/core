package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.viewmodel.TestViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelLifecycle
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class ListNavigatorContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: Context) : ViewModel(context) {

        val navigator = listNavigator()

    }

    class ChildModelNavigatorContext(context: Context) : ViewModel(context) {

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
        val childModel = ViewModel(rootModel.childContext())
        childModel.listNavigator(rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.childContext())
        childModel.listNavigator(rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModel(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.set(
            listOf(
                ViewModel(rootModel.context.childContext())
            )
        )
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.set(
            listOf(
                ViewModel(rootModel.context.childContext())
            )
        )
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContext() {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.set(
            listOf(
                ViewModel(childModelContext.childContext())
            )
        )
    }

}