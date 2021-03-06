package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import kotlin.test.Test

class StackNavigatorTest {

    private val context = createTestContext()

    private class RootViewModel(context: Context) : ViewModel(context) {

        val navigator = stackNavigator()

    }

    private fun createViewModel(): IViewModel {
        return ViewModel(root.childContext())
    }

    private val root = RootViewModel(context.childContext())
    private val lifecycleController = ViewModelLifecycleController(root)
    private val adapter = TestAdapterSingle()

    private fun assertNavigatorStackState(vararg items: IViewModel) {
        assert(root.navigator.size == items.size)
        items.forEachIndexed { index, viewModel ->
            assert(root.navigator.items.getOrNull(index) == viewModel)
        }
    }

    @Test
    fun shouldCreateOnAddAndTerminateOnRemove() {
        lifecycleController.setAttached(true)
        val page = createViewModel()
        root.navigator.add(page)
        assert(page.isStateAttached())
        assertNavigatorStackState(page)

        root.navigator.clear()
        assert(page.isTerminated())
        assertNavigatorStackState()

    }

    @Test
    fun shouldSeekParentLifecycle() {
        lifecycleController.setAttached(true)
        val page = createViewModel()
        root.navigator.add(page)
        assertNavigatorStackState(page)
        root.navigator.setAdapter(adapter)
        assert(adapter.activeModel == page)
        assert(page.isStateAttached())
        lifecycleController.setBound(true)
        assert(page.isStateBound())
        lifecycleController.setVisible(true)
        assert(page.isStateVisible())
        lifecycleController.setFocused(true)
        assert(page.isStateFocused())
        lifecycleController.setFocused(false)
        assert(page.isStateVisible())
        lifecycleController.setVisible(false)
        assert(page.isStateBound())
        lifecycleController.setBound(false)
        assert(page.isStateAttached())
        lifecycleController.setAttached(false)
        assert(page.isAttached().not() && page.isTerminated().not())
        root.terminate()
        assert(page.isTerminated())
    }

    @Test
    fun shouldSwitchStagesWithoutAdapter() {
        lifecycleController.setAttached(true)
        val page1 = createViewModel()
        root.navigator.add(page1)
        assert(page1.isStateAttached())
        assertNavigatorStackState(page1)

        val page2 = createViewModel()
        root.navigator.add(page2)
        assert(page2.isStateAttached())
        assertNavigatorStackState(page1, page2)

        val page3 = createViewModel()
        root.navigator.add(page3)
        assert(page3.isStateAttached())
        assertNavigatorStackState(page1, page2, page3)

        val page4 = createViewModel()
        root.navigator.add(page4)
        assert(page4.isStateAttached())
        assertNavigatorStackState(page1, page2, page3, page4)

        val page5 = createViewModel()
        root.navigator.add(page5)
        assert(page5.isStateAttached())
        assertNavigatorStackState(page1, page2, page3, page4, page5)

        val page6 = createViewModel()
        root.navigator.add(page6)
        assert(page6.isStateAttached())
        assertNavigatorStackState(page1, page2, page3, page4, page5, page6)

        root.navigator.remove(page2)

        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateAttached())
        assert(page4.isStateAttached())
        assert(page5.isStateAttached())
        assert(page6.isStateAttached())
        assertNavigatorStackState(page1, page3, page4, page5, page6)

        root.navigator.popBackStack()
        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateAttached())
        assert(page4.isStateAttached())
        assert(page5.isStateAttached())
        assert(page6.isTerminated())
        assertNavigatorStackState(page1, page3, page4, page5)

        root.navigator.popBackStackTo(page3)
        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateAttached())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assertNavigatorStackState(page1, page3)

        val page7 = createViewModel()
        root.navigator.replaceAll(page7)

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assert(page7.isStateAttached())
        assertNavigatorStackState(page7)
    }

    @Test
    fun shouldSwitchStagesWithAdapter() {
        lifecycleController.setAttached(true)
        lifecycleController.setBound(true)
        root.navigator.setAdapter(adapter)
        lifecycleController.setVisible(true)
        lifecycleController.setFocused(true)
        val page1 = createViewModel()
        root.navigator.add(page1)
        assert(page1.isStateFocused())
        assertNavigatorStackState(page1)
        assert(adapter.activeModel == page1)
        assert(adapter.lastOnFront == true)

        val page2 = createViewModel()
        root.navigator.add(page2)
        assert(page2.isStateFocused())
        assertNavigatorStackState(page1, page2)
        assert(adapter.activeModel == page2)
        assert(adapter.lastOnFront == true)

        val page3 = createViewModel()
        root.navigator.add(page3)
        assertNavigatorStackState(page1, page2, page3)
        assert(page3.isStateFocused())
        assert(adapter.activeModel == page3)
        assert(adapter.lastOnFront == true)

        val page4 = createViewModel()
        root.navigator.add(page4)
        assertNavigatorStackState(page1, page2, page3, page4)
        assert(page4.isStateFocused())
        assert(adapter.activeModel == page4)
        assert(adapter.lastOnFront == true)

        val page5 = createViewModel()
        root.navigator.add(page5)
        assertNavigatorStackState(page1, page2, page3, page4, page5)
        assert(page5.isStateFocused())
        assert(adapter.activeModel == page5)
        assert(adapter.lastOnFront == true)

        val page6 = createViewModel()
        root.navigator.add(page6)
        assert(page6.isStateFocused())
        assertNavigatorStackState(page1, page2, page3, page4, page5, page6)
        assert(adapter.activeModel == page6)
        assert(adapter.lastOnFront == true)

        root.navigator.remove(page2)

        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateAttached())
        assert(page4.isStateAttached())
        assert(page5.isStateAttached())
        assert(page6.isStateFocused())
        assertNavigatorStackState(page1, page3, page4, page5, page6)
        assert(adapter.activeModel == page6)
        assert(adapter.lastOnFront == true)

        root.navigator.popBackStack()
        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateAttached())
        assert(page4.isStateAttached())
        assert(page5.isStateFocused())
        assert(page6.isTerminated())
        assertNavigatorStackState(page1, page3, page4, page5)
        assert(adapter.activeModel == page5)
        assert(adapter.lastOnFront == false)

        root.navigator.popBackStackTo(page3)
        assert(page1.isStateAttached())
        assert(page2.isTerminated())
        assert(page3.isStateFocused())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assertNavigatorStackState(page1, page3)
        assert(adapter.activeModel == page3)
        assert(adapter.lastOnFront == false)

        val page7 = createViewModel()
        root.navigator.replaceAll(page7)
        assert(adapter.lastOnFront == true)

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assert(page7.isStateFocused())
        assertNavigatorStackState(page7)
        assert(adapter.activeModel == page7)
    }

    @Test
    fun shouldCorrectlyReplaceStack() {
        lifecycleController.setAttached(true)
        lifecycleController.setBound(true)
        root.navigator.setAdapter(adapter)
        val page1 = createViewModel()
        val page2 = createViewModel()
        val page3 = createViewModel()
        val page4 = createViewModel()
        root.navigator.add(page1)
        root.navigator.add(page2)
        root.navigator.add(page3)
        root.navigator.add(page4)
        assert(page1.isStateAttached())
        assert(page2.isStateAttached())
        assert(page3.isStateAttached())
        assert(page4.isStateBound())
        assertNavigatorStackState(page1, page2, page3, page4)
        assert(adapter.activeModel == page4)

        val page5 = createViewModel()
        val page6 = createViewModel()
        val page7 = createViewModel()
        root.navigator.replaceStack(listOf(page2, page3, page5, page6, page7))
        assert(page1.isTerminated())
        assert(page2.isStateAttached())
        assert(page3.isStateAttached())
        assert(page4.isTerminated())
        assert(page5.isStateAttached())
        assert(page6.isStateAttached())
        assert(page7.isStateBound())
        assertNavigatorStackState(page2, page3, page5, page6, page7)
        assert(adapter.activeModel == page7)

        val page8 = createViewModel()
        val page9 = createViewModel()
        root.navigator.updateStack {
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
        assert(page9.isStateBound())
        assertNavigatorStackState(page2, page5, page7, page8, page9)
        assert(adapter.activeModel == page9)
    }

}