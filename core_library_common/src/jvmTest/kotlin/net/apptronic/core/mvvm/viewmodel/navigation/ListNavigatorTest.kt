package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.component.terminate
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ListNavigatorTest {

    private val context = testContext { }
    private val rootViewModel = ViewModel(context.viewModelContext())
    private val items = rootViewModel.value<List<IViewModel>>(emptyList())
    private val navigator = rootViewModel.listNavigator(items)

    fun childModel() = ViewModel(rootViewModel.viewModelContext())

    private val adapter = TestListAdapter()
    private var listItems: List<IViewModel> = emptyList()
    private lateinit var status: ListNavigatorStatus

    fun addVerification() {
        navigator.setAdapter(adapter)
        navigator.subscribe {
            listItems = it
        }
        navigator.observeStatus().subscribe {
            status = it
        }
    }

    private fun verifyState() {
        verifyState(items.get())
    }

    private fun verifyState(itemsList: List<IViewModel>) {
        assertEquals(itemsList.size, navigator.getStatus().all.size)
        assertEquals(itemsList.size, status.all.size)
        assertEquals(itemsList.size, adapter.items.size)
        assertEquals(itemsList.size, listItems.size)
        itemsList.forEachIndexed { index, viewModel ->
            assertSame(viewModel, navigator.getStatus().all[index])
            assertSame(viewModel, status.all[index])
            assertSame(viewModel, adapter.items[index].viewModel)
            assertSame(viewModel, listItems[index])
        }
    }

    @Test
    fun shouldAddRemoveItemsCorrectly() {
        addVerification()

        verifyState()
        verifyState(emptyList())

        val child1 = childModel()
        val child2 = childModel()
        items.set(listOf(child1, child2))
        verifyState()
        verifyState(listOf(child1, child2))

        val child3 = childModel()
        val child4 = childModel()
        items.set(listOf(child1, child2, child3, child4))

        verifyState()
        verifyState(listOf(child1, child2, child3, child4))

        val child5 = childModel()
        items.set(listOf(child1, child3, child5))
        verifyState()
        verifyState(listOf(child1, child3, child5))

        items.set(emptyList())
        verifyState()
        verifyState(emptyList())
    }

    private fun IViewModel.isDefaultStage() = context.lifecycle.getActiveStage() == context.lifecycle.rootStage

    @Test
    fun shouldBeInDefaultStageWithParent() {
        val child1 = childModel()
        assertTrue(child1.isDefaultStage())

        items.set(listOf(child1))
        assertTrue(child1.isAttached())

        items.set(listOf())
        assertTrue(child1.isTerminated())
    }

    @Test
    fun shouldEnterAttachedStage() {
        rootViewModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)

        val child1 = childModel()
        assertTrue(child1.isDefaultStage())
        assertFalse(child1.isTerminated())

        items.set(listOf(child1))
        assertTrue(child1.isStateAttached())

        val child2 = childModel()
        val child3 = childModel()
        items.set(listOf(child1, child2, child3))
        assertTrue(child1.isStateAttached())
        assertTrue(child2.isStateAttached())
        assertTrue(child3.isStateAttached())

        items.set(listOf(child1, child3))
        assertTrue(child1.isStateAttached())
        assertTrue(child2.isTerminated())
        assertTrue(child3.isStateAttached())

        val child4 = childModel()
        items.set(listOf(child1, child3, child4))
        assertTrue(child1.isStateAttached())
        assertTrue(child2.isTerminated())
        assertTrue(child3.isStateAttached())
        assertTrue(child4.isStateAttached())

        items.set(listOf(child3, child4))
        assertTrue(child1.isTerminated())
        assertTrue(child2.isTerminated())
        assertTrue(child3.isStateAttached())
        assertTrue(child4.isStateAttached())

        items.set(emptyList())
        assertTrue(child1.isTerminated())
        assertTrue(child2.isTerminated())
        assertTrue(child3.isTerminated())
        assertTrue(child4.isTerminated())
    }

    @Test
    fun shouldTerminateWithParent() {
        rootViewModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        val child1 = childModel()
        items.set(listOf(child1))
        assertTrue(child1.isStateAttached())

        rootViewModel.terminate()
        assertTrue(child1.isTerminated())
    }

    @Test
    fun shouldIgnoreParentStagesWithoutAdapter() {
        rootViewModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        val child1 = childModel()
        items.set(listOf(child1))
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_VISIBLE)
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_FOCUSED)
        assertTrue(child1.isStateAttached())
    }

    @Test
    fun shouldMatchParentStageWithAdapter() {
        rootViewModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        navigator.setAdapter(adapter)

        val child1 = childModel()
        items.set(listOf(child1))
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_VISIBLE)
        assertTrue(child1.isStateAttached())

        rootViewModel.enterStage(ViewModelLifecycle.STAGE_FOCUSED)
        assertTrue(child1.isStateAttached())

        navigator.setBound(child1, true)
        assertTrue(child1.isStateBound())

        navigator.setVisible(child1, true)
        assertTrue(child1.isStateVisible())

        navigator.setFocused(child1, true)
        assertTrue(child1.isStateFocused())

        rootViewModel.exitStage(ViewModelLifecycle.STAGE_FOCUSED)
        assertTrue(child1.isStateVisible())

        rootViewModel.exitStage(ViewModelLifecycle.STAGE_VISIBLE)
        assertTrue(child1.isStateBound())
    }

}