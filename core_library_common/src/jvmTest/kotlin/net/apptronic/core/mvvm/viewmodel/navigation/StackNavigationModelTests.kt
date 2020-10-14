package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class StackNavigationModelTests {

    val context = testContext()
    val coreViewModel = ViewModel(context.viewModelContext())

    abstract val stackNavigationModel: IStackNavigationModel

    fun childViewModel(): IViewModel = ViewModel(coreViewModel.viewModelContext())

    lateinit var status: StackNavigatorContent

    var actualModel: IViewModel? = null
    val adapter = object : SingleViewModelAdapter {
        override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
            actualModel = item?.viewModel
        }
    }

    fun assertStack(vararg viewModels: IViewModel) {
        val stack = stackNavigationModel.getStack()
        assertEquals(viewModels.size, stack.size)
        stack.forEachIndexed { index, item ->
            assert(item === viewModels[index])
            assert(stackNavigationModel.getItemAt(index) === viewModels[index])
        }
        assert(status.size == viewModels.size)
        status.stack.forEachIndexed { index, item ->
            assert(item === viewModels[index])
            assert(stackNavigationModel.getItemAt(index) === viewModels[index])
        }
        assert(actualModel === viewModels.lastOrNull())
    }

    @Test
    fun verifySet() {
        assertStack()

        val child1 = childViewModel()
        stackNavigationModel.set(child1)
        assertStack(child1)

        val child2 = childViewModel()
        stackNavigationModel.add(child2)
        assertStack(child1, child2)

        val child3 = childViewModel()
        stackNavigationModel.set(child3)
        assertStack(child3)

        stackNavigationModel.set(null)
        assertStack()
    }

    @Test
    fun verifyClear() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.clear()
        assertStack()

        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)
        stackNavigationModel.clear()
        assertStack()
    }

    @Test
    fun verifyReplaceAll() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)

        val child4 = childViewModel()
        stackNavigationModel.replaceAll(child4)
        assertStack(child4)
    }

    @Test
    fun verifyAdd() {
        val child1 = childViewModel()
        stackNavigationModel.add(child1)
        assertStack(child1)

        val child2 = childViewModel()
        stackNavigationModel.add(child2)
        assertStack(child1, child2)

        val child3 = childViewModel()
        stackNavigationModel.add(child3)
        assertStack(child1, child2, child3)

        stackNavigationModel.popBackStack()
        val child4 = childViewModel()
        stackNavigationModel.add(child4)
        assertStack(child1, child2, child4)
    }

    @Test
    fun verifyReplace() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)

        val child4 = childViewModel()
        stackNavigationModel.replace(child4)
        assertStack(child1, child2, child4)
    }

    @Test
    fun verifyRemove() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        val child6 = childViewModel()
        val child7 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)
        stackNavigationModel.add(child6)
        stackNavigationModel.add(child7)

        stackNavigationModel.remove(child1)
        assertStack(child2, child3, child4, child5, child6, child7)

        stackNavigationModel.remove(child4)
        assertStack(child2, child3, child5, child6, child7)

        stackNavigationModel.remove(child7)
        assertStack(child2, child3, child5, child6)
    }

    @Test
    fun verifyRemoveAt() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        val child6 = childViewModel()
        val child7 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)
        stackNavigationModel.add(child6)
        stackNavigationModel.add(child7)

        stackNavigationModel.removeAt(0)
        assertStack(child2, child3, child4, child5, child6, child7)

        stackNavigationModel.removeAt(2)
        assertStack(child2, child3, child5, child6, child7)

        stackNavigationModel.removeAt(4)
        assertStack(child2, child3, child5, child6)
    }

    @Test
    fun verifyRemoveLast() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)

        assertTrue(stackNavigationModel.removeLast())
        assertStack(child1, child2, child3, child4)

        assertTrue(stackNavigationModel.removeLast())
        assertStack(child1, child2, child3)

        assertTrue(stackNavigationModel.removeLast())
        assertStack(child1, child2)

        assertTrue(stackNavigationModel.removeLast())
        assertStack(child1)

        assertTrue(stackNavigationModel.removeLast())
        assertStack()

        assertFalse(stackNavigationModel.removeLast())
    }

    @Test
    fun verifyPopBackStack() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)

        assertTrue(stackNavigationModel.popBackStack())
        assertStack(child1, child2, child3, child4)

        assertTrue(stackNavigationModel.popBackStack())
        assertStack(child1, child2, child3)

        assertTrue(stackNavigationModel.popBackStack())
        assertStack(child1, child2)

        assertTrue(stackNavigationModel.popBackStack())
        assertStack(child1)

        assertFalse(stackNavigationModel.popBackStack())
        assertStack(child1)

        stackNavigationModel.clear()
        assertStack()
        assertFalse(stackNavigationModel.popBackStack())
    }

    @Test
    fun verifyNavigateBack() {

        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)

        var invokedOn1 = false
        stackNavigationModel.navigateBack {
            invokedOn1 = true
        }
        assertFalse(invokedOn1)
        assertStack(child1, child2, child3, child4)

        var invokedOn2 = false
        stackNavigationModel.navigateBack {
            invokedOn2 = true
        }
        assertFalse(invokedOn1)
        assertStack(child1, child2, child3)


        var invokedOn3 = false
        stackNavigationModel.navigateBack {
            invokedOn3 = true
        }
        assertFalse(invokedOn3)
        assertStack(child1, child2)

        var invokedOn4 = false
        stackNavigationModel.navigateBack {
            invokedOn4 = true
        }
        assertFalse(invokedOn4)
        assertStack(child1)

        var invokedOn5 = false
        stackNavigationModel.navigateBack {
            invokedOn5 = true
        }
        assertTrue(invokedOn5)
        assertStack(child1)

        stackNavigationModel.clear()
        var invokedOn6 = false
        stackNavigationModel.navigateBack {
            invokedOn6 = true
        }
        assertTrue(invokedOn6)

    }

    @Test
    fun verifyPopBackStackTo() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        val child6 = childViewModel()
        val child7 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)
        stackNavigationModel.add(child6)
        stackNavigationModel.add(child7)
        assertStack(child1, child2, child3, child4, child5, child6, child7)

        assertFalse(stackNavigationModel.popBackStackTo(child7))
        assertStack(child1, child2, child3, child4, child5, child6, child7)

        assertTrue(stackNavigationModel.popBackStackTo(child6))
        assertStack(child1, child2, child3, child4, child5, child6)

        assertTrue(stackNavigationModel.popBackStackTo(child3))
        assertStack(child1, child2, child3)

        assertTrue(stackNavigationModel.popBackStackTo(child1))
        assertStack(child1)

        assertFalse(stackNavigationModel.popBackStackTo(child1))
        assertStack(child1)

        stackNavigationModel.clear()
        assertFalse(stackNavigationModel.popBackStackTo(child1))
        assertStack()
    }

    @Test
    fun verifyPopBackStackToIndex() {
        val child1 = childViewModel()
        val child2 = childViewModel()
        val child3 = childViewModel()
        val child4 = childViewModel()
        val child5 = childViewModel()
        val child6 = childViewModel()
        val child7 = childViewModel()
        stackNavigationModel.add(child1)
        stackNavigationModel.add(child2)
        stackNavigationModel.add(child3)
        stackNavigationModel.add(child4)
        stackNavigationModel.add(child5)
        stackNavigationModel.add(child6)
        stackNavigationModel.add(child7)
        assertStack(child1, child2, child3, child4, child5, child6, child7)

        assertFalse(stackNavigationModel.popBackStackTo(10))
        assertStack(child1, child2, child3, child4, child5, child6, child7)

        assertFalse(stackNavigationModel.popBackStackTo(6))
        assertStack(child1, child2, child3, child4, child5, child6, child7)

        assertTrue(stackNavigationModel.popBackStackTo(5))
        assertStack(child1, child2, child3, child4, child5, child6)

        assertTrue(stackNavigationModel.popBackStackTo(2))
        assertStack(child1, child2, child3)

        assertTrue(stackNavigationModel.popBackStackTo(0))
        assertStack(child1)

        assertFalse(stackNavigationModel.popBackStackTo(0))
        assertStack(child1)

        stackNavigationModel.clear()
        assertFalse(stackNavigationModel.popBackStackTo(0))
        assertStack()

        assertFalse(stackNavigationModel.popBackStackTo(0))
        assertStack()

        assertFalse(stackNavigationModel.popBackStackTo(-1))
        assertStack()

        assertFalse(stackNavigationModel.popBackStackTo(1))
        assertStack()
    }

    @Test
    fun shouldCorrectlyReplaceStack() {
        coreViewModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        val page1 = childViewModel()
        val page2 = childViewModel()
        val page3 = childViewModel()
        val page4 = childViewModel()
        stackNavigationModel.add(page1)
        stackNavigationModel.add(page2)
        stackNavigationModel.add(page3)
        stackNavigationModel.add(page4)
        assert(page1.isStateAttached())
        assert(page2.isStateAttached())
        assert(page3.isStateAttached())
        assert(page4.isStateAttached())
        assertStack(page1, page2, page3, page4)

        val page5 = childViewModel()
        val page6 = childViewModel()
        val page7 = childViewModel()
        stackNavigationModel.replaceStack(listOf(page2, page3, page5, page6, page7))
        assert(page1.isTerminated())
        assert(page2.isStateAttached())
        assert(page3.isStateAttached())
        assert(page4.isTerminated())
        assert(page5.isStateAttached())
        assert(page6.isStateAttached())
        assert(page7.isStateAttached())
        assertStack(page2, page3, page5, page6, page7)

        val page8 = childViewModel()
        val page9 = childViewModel()
        stackNavigationModel.updateStack {
            it.toMutableList().apply {
                remove(page3)
                remove(page6)
                add(page8)
                add(page9)
            }
        }
        assert(page1.isTerminated())
        assert(page2.isStateAttached())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isStateAttached())
        assert(page6.isTerminated())
        assert(page7.isStateAttached())
        assert(page8.isStateAttached())
        assert(page9.isStateAttached())
        assertStack(page2, page5, page7, page8, page9)
    }

}

class StackNavigatorVerificationTest : StackNavigationModelTests() {

    override val stackNavigationModel = coreViewModel.stackNavigator().apply {
        setAdapter(adapter)
        content.subscribe {
            status = it
        }
    }

}

class StackNavigationModelVerificationTest : StackNavigationModelTests() {

    override val stackNavigationModel = coreViewModel.stackNavigationModel().apply {
        setAdapter(adapter)
        content.subscribe {
            status = it
        }
    }

}