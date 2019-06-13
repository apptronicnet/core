package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.utils.TestContext
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

}