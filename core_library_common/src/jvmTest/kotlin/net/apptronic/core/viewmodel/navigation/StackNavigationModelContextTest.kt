package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.TestViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelLifecycle
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class StackNavigationModelContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: Context) : ViewModel(context) {

        val navigator = stackNavigator()

    }

    class ChildModelNavigatorContext(context: Context) : ViewModel(context) {

        val navigatorContext = childContext()
        val navigator = stackNavigator(navigatorContext)

    }

    @Test
    fun shouldCreateNavigatorWithNavigatorcontext() {
        rootModel.stackNavigator()
        rootModel.stackNavigator(rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext1() {
        val childModel = ViewModel(rootModel.childContext())
        childModel.stackNavigator(rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.childContext())
        childModel.stackNavigator(rootModel.childContext())
    }

    private fun shouldFailOnNonCorrectContext(onMethod: StackNavigationModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModel(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.onMethod(ViewModel(rootModel.context.childContext()))
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextSet() =
            shouldFailOnNonCorrectContext { set(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextAdd() =
            shouldFailOnNonCorrectContext { add(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextReplace() =
            shouldFailOnNonCorrectContext { replace(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextReplaceAll() =
            shouldFailOnNonCorrectContext { replaceAll(it) }

    private fun shouldFailOnNonCorrectContextWithNavigatorContext(onMethod: StackNavigationModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.onMethod(ViewModel(rootModel.context.childContext()))
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContextSet() =
            shouldFailOnNonCorrectContextWithNavigatorContext { set(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContextAdd() =
            shouldFailOnNonCorrectContextWithNavigatorContext { add(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContextReplace() =
            shouldFailOnNonCorrectContextWithNavigatorContext { replace(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectContextWithNavigatorContextReplaceAll() =
            shouldFailOnNonCorrectContextWithNavigatorContext { replaceAll(it) }

    private fun shouldFailOnNonCorrectOnNonNavigatorContext(onMethod: StackNavigationModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.childContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.Attached)
        childModel.enterStage(ViewModelLifecycle.Bound)
        childModel.navigator.onMethod(ViewModel(childModelContext.childContext()))
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContextSet() =
            shouldFailOnNonCorrectOnNonNavigatorContext { set(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContextAdd() =
            shouldFailOnNonCorrectOnNonNavigatorContext { add(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContextReplace() =
            shouldFailOnNonCorrectOnNonNavigatorContext { replace(it) }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailOnNonCorrectOnNonNavigatorContextReplaceAll() =
            shouldFailOnNonCorrectOnNonNavigatorContext { replaceAll(it) }

}