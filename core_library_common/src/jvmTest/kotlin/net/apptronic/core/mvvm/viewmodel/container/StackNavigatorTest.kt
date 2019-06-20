package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.testutils.TestContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import kotlin.test.Test

class StackNavigatorTest : TestContext() {

    private class RootViewModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = stackNavigator()

    }

    private fun createViewModel(): ViewModel {
        return ViewModel(ViewModelContext(this))
    }

    private val root = RootViewModel(ViewModelContext(this))
    private val lifecycleController = ViewModelLifecycleController(root)
    private val adapter = TestStackAdapter()

    @Test
    fun shouldCreateOnAddAndTerminateOnRemove() {
        lifecycleController.setCreated(true)
        val page = createViewModel()
        root.navigator.add(page)
        assert(page.isStateCreated())
        root.navigator.clear()
        assert(page.isTerminated())
    }

    @Test
    fun shouldSeekParentLifecycle() {
        lifecycleController.setCreated(true)
        val page = createViewModel()
        root.navigator.add(page)
        root.navigator.setAdapter(adapter)
        assert(adapter.activeModel == page)
        assert(page.isStateCreated())
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
        assert(page.isStateCreated())
        lifecycleController.setCreated(false)
        assert(page.isCreated().not() && page.isTerminated().not())
        root.terminate()
        assert(page.isTerminated())
    }

    @Test
    fun shouldSwitchStagesWithotAdapter() {
        lifecycleController.setCreated(true)
        val page1 = createViewModel()
        root.navigator.add(page1)
        assert(page1.isStateCreated())

        val page2 = createViewModel()
        root.navigator.add(page2)
        assert(page2.isStateCreated())

        val page3 = createViewModel()
        root.navigator.add(page3)
        assert(page3.isStateCreated())

        val page4 = createViewModel()
        root.navigator.add(page4)
        assert(page4.isStateCreated())

        val page5 = createViewModel()
        root.navigator.add(page5)
        assert(page5.isStateCreated())

        val page6 = createViewModel()
        root.navigator.add(page6)
        assert(page6.isStateCreated())

        root.navigator.remove(page2)

        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateCreated())
        assert(page4.isStateCreated())
        assert(page5.isStateCreated())
        assert(page6.isStateCreated())

        root.navigator.popBackStack()
        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateCreated())
        assert(page4.isStateCreated())
        assert(page5.isStateCreated())
        assert(page6.isTerminated())

        root.navigator.popBackStackTo(page3)
        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateCreated())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())

        val page7 = createViewModel()
        root.navigator.replaceAll(page7)

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assert(page7.isStateCreated())
    }

    @Test
    fun shouldSwitchStagesWithAdapter() {
        lifecycleController.setCreated(true)
        lifecycleController.setBound(true)
        root.navigator.setAdapter(adapter)
        lifecycleController.setVisible(true)
        lifecycleController.setFocused(true)
        val page1 = createViewModel()
        root.navigator.add(page1)
        assert(page1.isStateFocused())
        assert(adapter.activeModel == page1)

        val page2 = createViewModel()
        root.navigator.add(page2)
        assert(page2.isStateFocused())
        assert(adapter.activeModel == page2)

        val page3 = createViewModel()
        root.navigator.add(page3)
        assert(page3.isStateFocused())
        assert(adapter.activeModel == page3)

        val page4 = createViewModel()
        root.navigator.add(page4)
        assert(page4.isStateFocused())
        assert(adapter.activeModel == page4)

        val page5 = createViewModel()
        root.navigator.add(page5)
        assert(page5.isStateFocused())
        assert(adapter.activeModel == page5)

        val page6 = createViewModel()
        root.navigator.add(page6)
        assert(page6.isStateFocused())
        assert(adapter.activeModel == page6)

        root.navigator.remove(page2)

        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateCreated())
        assert(page4.isStateCreated())
        assert(page5.isStateCreated())
        assert(page6.isStateFocused())
        assert(adapter.activeModel == page6)

        root.navigator.popBackStack()
        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateCreated())
        assert(page4.isStateCreated())
        assert(page5.isStateFocused())
        assert(page6.isTerminated())

        root.navigator.popBackStackTo(page3)
        assert(page1.isStateCreated())
        assert(page2.isTerminated())
        assert(page3.isStateFocused())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assert(adapter.activeModel == page3)

        val page7 = createViewModel()
        root.navigator.replaceAll(page7)

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isTerminated())
        assert(page6.isTerminated())
        assert(page7.isStateFocused())
        assert(adapter.activeModel == page7)
    }

}