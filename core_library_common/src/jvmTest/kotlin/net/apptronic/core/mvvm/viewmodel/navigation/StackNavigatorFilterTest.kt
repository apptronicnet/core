package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.testutils.TestContext
import org.junit.Test

class StackNavigatorFilterTest : TestContext() {

    private class RootViewModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = stackNavigator().also {
            it.setSimpleVisibilityFilter()
        }

    }

    private class FilterableViewModel(context: ViewModelContext, val name: String) : ViewModel(context),
            ViewModelWithVisibility {

        val isReadyToShow = value<Boolean>(false)

        override fun isReadyToShow(): Entity<Boolean> {
            return isReadyToShow
        }

        override fun toString(): String {
            return "FilterableViewModel-$name"
        }

    }

    private fun createViewModel(name: String): FilterableViewModel {
        return FilterableViewModel(ViewModelContext(this), name)
    }

    private val root = RootViewModel(ViewModelContext(this))
    private val lifecycleController = ViewModelLifecycleController(root)
    private val adapter = TestStackAdapter()

    @Test
    fun shouldAddCorrectly() {
        lifecycleController.setCreated(true)
        val page1 = createViewModel("Page1")
        root.navigator.add(page1)
        assert(page1.isStateCreated())
        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == null)
        page1.isReadyToShow.set(true)
        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == page1)
        root.navigator.clear()
        assert(page1.isTerminated())
    }

    private fun assertNavigatorStackState(vararg items: ViewModel) {
        assert(root.navigator.getSize() == items.size)
        items.forEachIndexed { index, viewModel ->
            assert(root.navigator.getItemAt(index) == viewModel)
        }
    }

    @Test
    fun shouldPerformNavigationCorrectly() {
        lifecycleController.setCreated(true)

        // transition 1

        val page1 = createViewModel("Page1")
        root.navigator.add(page1)
        assert(page1.isStateCreated())
        assertNavigatorStackState(page1)

        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == null)

        root.navigator.setAdapter(adapter)
        lifecycleController.setVisible(true)
        assert(adapter.activeModel == null)

        page1.isReadyToShow.set(true)

        assert(page1.isStateVisible())
        assertNavigatorStackState(page1)

        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == page1)

        assert(adapter.activeModel == page1)

        // transition 2

        val page2 = createViewModel("Page2")
        root.navigator.add(page2, "from_1to_2")

        assert(page1.isStateVisible())
        assert(page2.isStateCreated())
        assertNavigatorStackState(page1, page2)

        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page2)
        assert(root.navigator.get().visibleModel == page1)
        assert(adapter.activeModel == page1)
        assert(adapter.lastTransition == null)

        page2.isReadyToShow.set(true)

        assert(page1.isStateCreated())
        assert(page2.isStateVisible())
        assertNavigatorStackState(page1, page2)

        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page2)
        assert(root.navigator.get().visibleModel == page2)
        assert(adapter.activeModel == page2)
        assert(adapter.lastTransition == "from_1to_2")

        // transition 3

        val page3 = createViewModel("Page3")
        root.navigator.add(page3, "from_2to_3")

        assert(page1.isStateCreated())
        assert(page2.isStateVisible())
        assert(page3.isStateCreated())
        assertNavigatorStackState(page1, page2, page3)

        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page3)
        assert(root.navigator.get().visibleModel == page2)
        assert(adapter.activeModel == page2)
        assert(adapter.lastTransition == "from_1to_2")

        page3.isReadyToShow.set(true)

        assert(page1.isStateCreated())
        assert(page2.isStateCreated())
        assert(page3.isStateVisible())
        assertNavigatorStackState(page1, page2, page3)

        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page3)
        assert(root.navigator.get().visibleModel == page3)
        assert(adapter.activeModel == page3)
        assert(adapter.lastTransition == "from_2to_3")

        // transition 4

        val page4 = createViewModel("Page4")
        root.navigator.replace(page4, "from_3to_4")

        assert(page1.isStateCreated())
        assert(page2.isStateCreated())
        assert(page3.isStateVisible())
        assert(page4.isStateCreated())
        assertNavigatorStackState(page1, page2, page4)

        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page4)
        assert(root.navigator.get().visibleModel == page3)
        assert(adapter.activeModel == page3)
        assert(adapter.lastTransition == "from_2to_3")

        page4.isReadyToShow.set(true)

        assert(page1.isStateCreated())
        assert(page2.isStateCreated())
        assert(page3.isTerminated())
        assert(page4.isStateVisible())
        assertNavigatorStackState(page1, page2, page4)

        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page4)
        assert(root.navigator.get().visibleModel == page4)
        assert(adapter.activeModel == page4)
        assert(adapter.lastTransition == "from_3to_4")

        // transition 5

        val page5 = createViewModel("Page5")
        root.navigator.replaceAll(page5, "from_4to_5")

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isStateVisible())
        assert(page5.isStateCreated())
        assertNavigatorStackState(page5)

        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page5)
        assert(root.navigator.get().visibleModel == page4)
        assert(adapter.activeModel == page4)
        assert(adapter.lastTransition == "from_3to_4")

        page5.isReadyToShow.set(true)

        assert(page1.isTerminated())
        assert(page2.isTerminated())
        assert(page3.isTerminated())
        assert(page4.isTerminated())
        assert(page5.isStateVisible())
        assertNavigatorStackState(page5)

        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page5)
        assert(root.navigator.get().visibleModel == page5)
        assert(adapter.activeModel == page5)
        assert(adapter.lastTransition == "from_4to_5")
    }

}