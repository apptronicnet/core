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

    private class FilterableViewModel(context: ViewModelContext) : ViewModel(context),
            ViewModelWithVisibility {

        val isReadyToShow = value<Boolean>(false)

        override fun isReadyToShow(): Entity<Boolean> {
            return isReadyToShow
        }

    }

    private fun createViewModel(): FilterableViewModel {
        return FilterableViewModel(ViewModelContext(this))
    }

    private val root = RootViewModel(ViewModelContext(this))
    private val lifecycleController = ViewModelLifecycleController(root)
    private val adapter = TestStackAdapter()

    @Test
    fun shouldAddCorrectly() {
        lifecycleController.setCreated(true)
        val page1 = createViewModel()
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

    @Test
    fun shouldAddToAdapterCorrectly() {
        lifecycleController.setCreated(true)
        val page1 = createViewModel()
        root.navigator.add(page1)
        assert(page1.isStateCreated())
        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == null)

        root.navigator.setAdapter(adapter)
        lifecycleController.setVisible(true)
        assert(adapter.activeModel == null)

        page1.isReadyToShow.set(true)
        assert(page1.isStateVisible())
        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page1)
        assert(root.navigator.get().visibleModel == page1)

        assert(adapter.activeModel == page1)

        val page2 = createViewModel()
        root.navigator.add(page2, "from_1to_2")

        assert(page1.isStateVisible())
        assert(page2.isStateCreated())
        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page2)
        assert(root.navigator.get().visibleModel == page1)
        assert(adapter.activeModel == page1)
        assert(adapter.lastTransition == null)

        page2.isReadyToShow.set(true)

        assert(page1.isStateCreated())
        assert(page2.isStateVisible())
        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page2)
        assert(root.navigator.get().visibleModel == page2)
        assert(adapter.activeModel == page2)
        assert(adapter.lastTransition == "from_1to_2")

        val page3 = createViewModel()
        root.navigator.add(page3, "from_2to_3")

        assert(page1.isStateCreated())
        assert(page2.isStateVisible())
        assert(page3.isStateCreated())
        assert(root.navigator.get().isInProgress)
        assert(root.navigator.get().actualModel == page3)
        assert(root.navigator.get().visibleModel == page2)
        assert(adapter.activeModel == page2)
        assert(adapter.lastTransition == "from_1to_2")

        page3.isReadyToShow.set(true)

        assert(page1.isStateCreated())
        assert(page2.isStateCreated())
        assert(page3.isStateVisible())
        assert(root.navigator.get().isInProgress.not())
        assert(root.navigator.get().actualModel == page3)
        assert(root.navigator.get().visibleModel == page3)
        assert(adapter.activeModel == page3)
        assert(adapter.lastTransition == "from_2to_3")
    }

}