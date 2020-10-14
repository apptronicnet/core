package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.*

class SelectorNavigationSingleAdapterModelTest {

    private val context = testContext()
    private val rootModel = RootModel(context.viewModelContext())
    private val navigator = rootModel.navigator
    private val adapter = SingleAdapter()

    private class SomeViewModel(context: ViewModelContext, val identifier: String) : ViewModel(context) {
        override fun toString(): String {
            return "ViewModel[$identifier]"
        }
    }

    private fun someViewModel(identifier: String) = SomeViewModel(navigator.context.viewModelContext(), identifier)

    private val vm1 = someViewModel("vm1")
    private val vm2 = someViewModel("vm2")
    private val vm3 = someViewModel("vm3")
    private val vm4 = someViewModel("vm4")
    private val vm5 = someViewModel("vm5")
    private val vm6 = someViewModel("vm6")
    private val vm7 = someViewModel("vm7")
    private val vm8 = someViewModel("vm8")
    private val vm9 = someViewModel("vm9")

    class RootModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = selectorNavigator()

    }

    class SingleAdapter : SingleViewModelAdapter {

        var currentItem: IViewModel? = null
        var lastTransition: Any? = null
        var invalidated: Boolean = false

        override fun onInvalidate(newModel: IViewModel?, transitionInfo: TransitionInfo) {
            currentItem = newModel
            lastTransition = transitionInfo.spec
            invalidated = true
        }

    }

    private val transition1 = Any()
    private val transition2 = Any()
    private val transition3 = Any()
    private val transition4 = Any()

    private fun assertAdapterInvalidatedTo(viewModel: IViewModel?, transitionSpec: Any?) {
        assertTrue(adapter.invalidated)
        assertSame(adapter.currentItem, viewModel)
        assertSame(adapter.lastTransition, transitionSpec)
        adapter.invalidated = false
    }

    private fun assertAdapterNotChanged() {
        assertFalse(adapter.invalidated)
    }

    private fun assertContent(items: Array<IViewModel>, focused: IViewModel?) {
        assertListEquals(navigator.list, items.toList())
        assertEquals(navigator.size, items.size)
        assertEquals(navigator.lastIndex, items.size - 1)
        items.forEach { viewModel ->
            if (focused === viewModel) {
                assertTrue(viewModel.isStateFocused())
            } else {
                assertTrue(viewModel.isStateAttached())
            }
        }
    }

    @Test
    fun verifySelectorNavigator() {
        rootModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        rootModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        navigator.setAdapter(adapter)
        rootModel.enterStage(ViewModelLifecycle.STAGE_VISIBLE)
        rootModel.enterStage(ViewModelLifecycle.STAGE_FOCUSED)

        navigator.set(vm1)
        assertAdapterInvalidatedTo(vm1, null)
        assertContent(arrayOf(vm1), vm1)

        navigator.add(vm2, "Not-called")
        assertAdapterNotChanged()
        assertContent(arrayOf(vm1, vm2), vm1)

        navigator.update(transition2, selectorIndex = SelectorNavigationModel.SELECTOR_LAST) {
            it.add(vm3)
            it.add(vm4)
            it.add(vm5)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(vm6, transition2)
        assertContent(arrayOf(vm1, vm2, vm3, vm4, vm5, vm6), vm6)

        navigator.setSelectorIndex(3, transition3)
        assertAdapterInvalidatedTo(vm4, transition3)
        assertContent(arrayOf(vm1, vm2, vm3, vm4, vm5, vm6), vm4)

        navigator.removeAt(2, selectorIndex = SelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm1, vm2, vm4, vm5, vm6), vm4)
        assertTrue(vm3.isTerminated())
    }

    fun assertTerminated(vararg items: IViewModel) {
        items.forEach {
            assertTrue(it.isTerminated())
        }
    }

    fun initDefaults() {
        rootModel.enterStage(ViewModelLifecycle.STAGE_ATTACHED)
        rootModel.enterStage(ViewModelLifecycle.STAGE_BOUND)
        navigator.setAdapter(adapter)
        rootModel.enterStage(ViewModelLifecycle.STAGE_VISIBLE)
        rootModel.enterStage(ViewModelLifecycle.STAGE_FOCUSED)
        navigator.set(listOf(vm1, vm2, vm3, vm4, vm5), SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm1, vm2, vm3, vm4, vm5), null)
    }

    @Test
    fun verifySetExisting1() {
        initDefaults()
        navigator.set(vm2, false)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm2), null)
        assertTerminated(vm1, vm3, vm4, vm5)
    }

    @Test
    fun verifySetExisting2() {
        initDefaults()
        navigator.set(vm2, true)
        assertAdapterInvalidatedTo(vm2, null)
        assertContent(arrayOf(vm2), vm2)
        assertTerminated(vm1, vm3, vm4, vm5)
    }

    @Test
    fun verifySetExisting3() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm3, transition1)
        navigator.set(vm2, false)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm2), null)
        assertTerminated(vm1, vm3, vm4, vm5)
    }

    @Test
    fun verifySetExisting4() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm3, transition1)
        navigator.set(vm2, true)
        assertAdapterInvalidatedTo(vm2, null)
        assertContent(arrayOf(vm2), vm2)
        assertTerminated(vm1, vm3, vm4, vm5)
    }

    @Test
    fun verifySetNew1() {
        initDefaults()
        navigator.set(vm7, false)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm7), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifySetNew2() {
        initDefaults()
        navigator.set(vm7, true)
        assertAdapterInvalidatedTo(vm7, null)
        assertContent(arrayOf(vm7), vm7)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifySetNew3() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm3, transition1)
        navigator.set(vm7, false)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm7), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifySetNew4() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm3, transition1)
        navigator.set(vm7, true)
        assertAdapterInvalidatedTo(vm7, null)
        assertContent(arrayOf(vm7), vm7)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifySetListIndex() {
        initDefaults()
        navigator.set(listOf(vm3, vm2, vm6, vm7, vm8), selectorIndex = 3)
        assertAdapterInvalidatedTo(vm7, null)
        assertContent(arrayOf(vm3, vm2, vm6, vm7, vm8), vm7)
        assertTerminated(vm1, vm4, vm5)
    }

    @Test
    fun verifySetListNothing() {
        initDefaults()
        navigator.setSelectorIndex(4, transition1)
        assertAdapterInvalidatedTo(vm5, transition1)

        navigator.set(listOf(vm3, vm2, vm6, vm7, vm8), SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm3, vm2, vm6, vm7, vm8), null)
        assertTerminated(vm1, vm4, vm5)
    }

    @Test
    fun verifySetListSameIndex() {
        initDefaults()
        navigator.setSelectorIndex(4, transition1)
        assertAdapterInvalidatedTo(vm5, transition1)

        navigator.set(listOf(vm3, vm2, vm6, vm7, vm8), selectorIndex = SelectorNavigationModel.SELECTOR_SAME_POSITION)
        assertAdapterInvalidatedTo(vm8, null)
        assertContent(arrayOf(vm3, vm2, vm6, vm7, vm8), vm8)
        assertTerminated(vm1, vm4, vm5)
    }

    @Test
    fun verifySetListSameItem() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.set(listOf(vm6, vm7, vm3, vm2, vm8), selectorIndex = SelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm6, vm7, vm3, vm2, vm8), vm2)
        assertTerminated(vm1, vm4, vm5)
    }

    @Test
    fun verifyClearNotShowing() {
        initDefaults()
        navigator.clear()
        assertAdapterNotChanged()
        assertContent(arrayOf(), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyClearShowing() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.clear()
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyReplaceShowingAllToNew() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.replaceAll(vm8, transition2)
        assertAdapterInvalidatedTo(vm8, transition2)
        assertContent(arrayOf(vm8), vm8)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyReplaceNotShowingAllToNew() {
        initDefaults()

        navigator.replaceAll(vm8, transition2)
        assertAdapterInvalidatedTo(vm8, transition2)
        assertContent(arrayOf(vm8), vm8)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyReplaceShowingAllToNewHide() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.replaceAll(vm8, transition2, selectorIndex = SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, transition2)
        assertContent(arrayOf(vm8), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyReplaceNotShowingAllToNewHide() {
        initDefaults()

        navigator.replaceAll(vm8, transition2, selectorIndex = SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm8), null)
        assertTerminated(vm1, vm2, vm3, vm4, vm5)
    }

    @Test
    fun verifyReplaceShowingAllToExisting() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.replaceAll(vm3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)
        assertContent(arrayOf(vm3), vm3)
        assertTerminated(vm1, vm2, vm4, vm5)
    }

    @Test
    fun verifyReplaceNotShowingAllToExisting() {
        initDefaults()

        navigator.replaceAll(vm3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)
        assertContent(arrayOf(vm3), vm3)
        assertTerminated(vm1, vm2, vm4, vm5)
    }

    @Test
    fun verifyReplaceShowingAllToExistingHide() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)

        navigator.replaceAll(vm3, transition2, selectorIndex = SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, transition2)
        assertContent(arrayOf(vm3), null)
        assertTerminated(vm1, vm2, vm4, vm5)
    }

    @Test
    fun verifyReplaceNotShowingAllToExistingHide() {
        initDefaults()

        navigator.replaceAll(vm3, transition2, selectorIndex = SelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm3), null)
        assertTerminated(vm1, vm2, vm4, vm5)
    }

    @Test
    fun verifyReplaceShowingAllToShowSame() {
        initDefaults()
        navigator.setSelectorIndex(2, transition3)
        assertAdapterInvalidatedTo(vm3, transition3)

        navigator.replaceAll(vm3, transition2, selectorIndex = SelectorNavigationModel.SELECTOR_SAME_POSITION)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm3), vm3)
        assertTerminated(vm1, vm2, vm4, vm5)
    }

    @Test
    fun notWrittenYet() {
        fail("Write complete tests")
    }

}