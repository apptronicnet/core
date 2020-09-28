package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.mvvm.TestViewModel
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import org.junit.Test

/**
 * Adding [ViewModel] to navigator should not allow using of different parent context than navigator context.
 */
class StackNavigationViewModelContextTest : TestViewModel() {

    val rootModel = TestViewModel()

    class ChildModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = stackNavigationModel()

    }

    class ChildModelNavigatorContext(context: ViewModelContext) : ViewModel(context) {

        val navigatorContext = childContext()
        val navigator = stackNavigationModel(navigatorContext)

    }

    @Test
    fun shouldCreateNavigatorWithNavigatorcontext() {
        rootModel.stackNavigationModel()
        rootModel.stackNavigationModel(rootModel.childContext())
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext1() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.stackNavigationModel(rootModel.context)
    }

    @Test(expected = IncorrectContextHierarchyException::class)
    fun shouldFailCreateNavigatorWithIncorrectContext2() {
        val childModel = ViewModel(rootModel.viewModelContext())
        childModel.stackNavigationModel(rootModel.childContext())
    }

    private fun shouldFailOnNonCorrectContext(onMethod: StackNavigationViewModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModel(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.onMethod(ViewModel(rootModel.context.viewModelContext()))
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

    private fun shouldFailOnNonCorrectContextWithNavigatorContext(onMethod: StackNavigationViewModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.onMethod(ViewModel(rootModel.context.viewModelContext()))
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

    private fun shouldFailOnNonCorrectOnNonNavigatorContext(onMethod: StackNavigationViewModel.(IViewModel) -> Unit) {
        rootModel.attach()
        rootModel.bind()
        val childModelContext = rootModel.viewModelContext()
        val childModel = ChildModelNavigatorContext(childModelContext)
        childModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        childModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        childModel.navigator.onMethod(ViewModel(childModelContext.viewModelContext()))
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