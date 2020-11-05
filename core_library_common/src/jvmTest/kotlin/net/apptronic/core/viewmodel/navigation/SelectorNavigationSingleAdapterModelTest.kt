package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.viewModelContext
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.ViewModelLifecycle
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SelectorNavigationSingleAdapterModelTest {

    private val context = createTestContext()
    private val rootModel = RootModel(context.viewModelContext())
    private val navigator = rootModel.navigator
    private val adapter = SingleAdapter()

    private class SomeViewModel(context: ViewModelContext, val identifier: String) : ViewModel(context) {
        override fun toString(): String {
            return "ViewModel[$identifier]"
        }
    }

    private fun someViewModel(identifier: String) = SomeViewModel(navigator.navigatorContext.viewModelContext(), identifier)

    private val vm0 = someViewModel("vm0")
    private val vm1 = someViewModel("vm1")
    private val vm2 = someViewModel("vm2")
    private val vm3 = someViewModel("vm3")
    private val vm4 = someViewModel("vm4")
    private val vm5 = someViewModel("vm5")
    private val vm6 = someViewModel("vm6")
    private val vm7 = someViewModel("vm7")
    private val vm8 = someViewModel("vm8")

    class RootModel(context: ViewModelContext) : ViewModel(context) {

        val navigator = selectorNavigator()

    }

    class SingleAdapter : SingleViewModelAdapter {

        var currentItem: IViewModel? = null
        var lastTransition: Any? = null
        var invalidated: Boolean = false

        override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
            currentItem = item?.viewModel
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
        assertListEquals(navigator.items, items.toList())
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

        navigator.set(vm0)
        assertAdapterInvalidatedTo(vm0, null)
        assertContent(arrayOf(vm0), vm0)

        navigator.add(vm1, navigator.size, "Not-called")
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1), vm0)

        navigator.update(transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_LAST) {
            it.add(vm2)
            it.add(vm3)
            it.add(vm4)
            it.add(vm5)
        }
        assertAdapterInvalidatedTo(vm5, transition2)
        assertContent(arrayOf(vm0, vm1, vm2, vm3, vm4, vm5), vm5)

        navigator.setSelectorIndex(3, transition3)
        assertAdapterInvalidatedTo(vm3, transition3)
        assertContent(arrayOf(vm0, vm1, vm2, vm3, vm4, vm5), vm3)

        navigator.removeAt(2, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm3, vm4, vm5), vm3)
        assertTrue(vm2.isTerminated())
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
        navigator.set(listOf(vm0, vm1, vm2, vm3, vm4), ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm0, vm1, vm2, vm3, vm4), null)
    }

    @Test
    fun verifySetExisting1() {
        initDefaults()
        navigator.set(vm1, false)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm1), null)
        assertTerminated(vm0, vm2, vm3, vm4)
    }

    @Test
    fun verifySetExisting2() {
        initDefaults()
        navigator.set(vm1, true)
        assertAdapterInvalidatedTo(vm1, null)
        assertContent(arrayOf(vm1), vm1)
        assertTerminated(vm0, vm2, vm3, vm4)
    }

    @Test
    fun verifySetExisting3() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)
        navigator.set(vm1, false)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm1), null)
        assertTerminated(vm0, vm2, vm3, vm4)
    }

    @Test
    fun verifySetExisting4() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)
        navigator.set(vm1, true)
        assertAdapterInvalidatedTo(vm1, null)
        assertContent(arrayOf(vm1), vm1)
        assertTerminated(vm0, vm2, vm3, vm4)
    }

    @Test
    fun verifySetNew1() {
        initDefaults()
        navigator.set(vm6, false)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm6), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifySetNew2() {
        initDefaults()
        navigator.set(vm6, true)
        assertAdapterInvalidatedTo(vm6, null)
        assertContent(arrayOf(vm6), vm6)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifySetNew3() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)
        navigator.set(vm6, false)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm6), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifySetNew4() {
        initDefaults()
        navigator.setSelectorIndex(2, transition1)
        assertAdapterInvalidatedTo(vm2, transition1)
        navigator.set(vm6, true)
        assertAdapterInvalidatedTo(vm6, null)
        assertContent(arrayOf(vm6), vm6)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifySetListIndex() {
        initDefaults()
        navigator.set(listOf(vm2, vm1, vm5, vm6, vm7), selectorIndex = 3)
        assertAdapterInvalidatedTo(vm6, null)
        assertContent(arrayOf(vm2, vm1, vm5, vm6, vm7), vm6)
        assertTerminated(vm0, vm3, vm4)
    }

    @Test
    fun verifySetListNothing() {
        initDefaults()
        navigator.setSelectorIndex(4, transition1)
        assertAdapterInvalidatedTo(vm4, transition1)

        navigator.set(listOf(vm2, vm1, vm5, vm6, vm7), ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(vm2, vm1, vm5, vm6, vm7), null)
        assertTerminated(vm0, vm3, vm4)
    }

    @Test
    fun verifySetListSameIndex() {
        initDefaults()
        navigator.setSelectorIndex(4, transition1)
        assertAdapterInvalidatedTo(vm4, transition1)

        navigator.set(listOf(vm2, vm1, vm5, vm6, vm7), selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_POSITION)
        assertAdapterInvalidatedTo(vm7, null)
        assertContent(arrayOf(vm2, vm1, vm5, vm6, vm7), vm7)
        assertTerminated(vm0, vm3, vm4)
    }

    @Test
    fun verifySetListSameItem() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.set(listOf(vm5, vm6, vm2, vm1, vm7), selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm5, vm6, vm2, vm1, vm7), vm1)
        assertTerminated(vm0, vm3, vm4)
    }

    @Test
    fun verifyClearNotShowing() {
        initDefaults()
        navigator.clear()
        assertAdapterNotChanged()
        assertContent(arrayOf(), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyClearShowing() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.clear()
        assertAdapterInvalidatedTo(null, null)
        assertContent(arrayOf(), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyReplaceShowingAllToNew() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.replaceAll(vm7, transition2)
        assertAdapterInvalidatedTo(vm7, transition2)
        assertContent(arrayOf(vm7), vm7)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyReplaceNotShowingAllToNew() {
        initDefaults()

        navigator.replaceAll(vm7, transition2)
        assertAdapterInvalidatedTo(vm7, transition2)
        assertContent(arrayOf(vm7), vm7)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyReplaceShowingAllToNewHide() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.replaceAll(vm7, transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, transition2)
        assertContent(arrayOf(vm7), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyReplaceNotShowingAllToNewHide() {
        initDefaults()

        navigator.replaceAll(vm7, transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm7), null)
        assertTerminated(vm0, vm1, vm2, vm3, vm4)
    }

    @Test
    fun verifyReplaceShowingAllToExisting() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.replaceAll(vm2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)
        assertContent(arrayOf(vm2), vm2)
        assertTerminated(vm0, vm1, vm3, vm4)
    }

    @Test
    fun verifyReplaceNotShowingAllToExisting() {
        initDefaults()

        navigator.replaceAll(vm2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)
        assertContent(arrayOf(vm2), vm2)
        assertTerminated(vm0, vm1, vm3, vm4)
    }

    @Test
    fun verifyReplaceShowingAllToExistingHide() {
        initDefaults()
        navigator.setSelectorIndex(1, transition1)
        assertAdapterInvalidatedTo(vm1, transition1)

        navigator.replaceAll(vm2, transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterInvalidatedTo(null, transition2)
        assertContent(arrayOf(vm2), null)
        assertTerminated(vm0, vm1, vm3, vm4)
    }

    @Test
    fun verifyReplaceNotShowingAllToExistingHide() {
        initDefaults()

        navigator.replaceAll(vm2, transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_NOTHING)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm2), null)
        assertTerminated(vm0, vm1, vm3, vm4)
    }

    @Test
    fun verifyReplaceShowingAllToShowSame() {
        initDefaults()
        navigator.setSelectorIndex(2, transition3)
        assertAdapterInvalidatedTo(vm2, transition3)

        navigator.replaceAll(vm2, transition2, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_POSITION)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm2), vm2)
        assertTerminated(vm0, vm1, vm3, vm4)
    }

    @Test
    fun verifyRemoveShowing() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.remove(vm2, transition4)
        assertAdapterInvalidatedTo(vm3, transition4)
        assertContent(arrayOf(vm0, vm1, vm3, vm4), vm3)
        assertTerminated(vm2)
    }

    @Test
    fun verifyRemoveNotShowing() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.remove(vm3, transition4)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm4), vm2)
        assertTerminated(vm3)
    }

    @Test
    fun verifyRemoveWhenNothingShowing() {
        initDefaults()

        navigator.remove(vm3, transition4)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm4), null)
        assertTerminated(vm3)
    }

    @Test
    fun verifyRemoveCurrentKeepItem() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.remove(vm2, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterInvalidatedTo(null, transition4)
        assertContent(arrayOf(vm0, vm1, vm3, vm4), null)
        assertTerminated(vm2)
    }

    @Test
    fun verifyRemoveKeepItem() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.remove(vm0, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm1, vm2, vm3, vm4), vm2)
        assertTerminated(vm0)
    }

    @Test
    fun verifyRemoveNotAdded() {
        initDefaults()

        navigator.remove(vm6, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm3, vm4), null)
    }

    @Test
    fun verifyRemoveNotShowingByIndex() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.removeAt(3, transition4)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm4), vm2)
        assertTerminated(vm3)
    }

    @Test
    fun verifyRemoveWhenNothingShowingByIndex() {
        initDefaults()

        navigator.removeAt(3, transition4)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm4), null)
        assertTerminated(vm3)
    }

    @Test
    fun verifyRemoveKeepItemByIndex() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.removeAt(0, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm1, vm2, vm3, vm4), vm2)
        assertTerminated(vm0)
    }

    @Test
    fun verifyRemoveCurrentKeepItemByIndex() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.removeAt(2, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterInvalidatedTo(null, transition4)
        assertContent(arrayOf(vm0, vm1, vm3, vm4), null)
        assertTerminated(vm2)
    }

    @Test
    fun verifyReplaceHidden() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replace(vm3, vm6, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm6, vm4), vm2)
        assertTerminated(vm3)
    }

    @Test
    fun verifyReplaceShowing() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replace(vm2, vm6, transition4)
        assertAdapterInvalidatedTo(vm6, transition4)
        assertContent(arrayOf(vm0, vm1, vm6, vm3, vm4), vm6)
        assertTerminated(vm2)
    }

    @Test
    fun verifyReplaceShowingTrySameItem() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replace(vm2, vm6, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterInvalidatedTo(null, transition4)
        assertContent(arrayOf(vm0, vm1, vm6, vm3, vm4), null)
        assertTerminated(vm2)
    }

    @Test
    fun verifyReplaceHiddenAtPosition() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replaceAt(3, vm6, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm2, vm6, vm4), vm2)
        assertTerminated(vm3)
    }

    @Test
    fun verifyReplaceShowingAtPosition() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replaceAt(2, vm6, transition4)
        assertAdapterInvalidatedTo(vm6, transition4)
        assertContent(arrayOf(vm0, vm1, vm6, vm3, vm4), vm6)
        assertTerminated(vm2)
    }

    @Test
    fun verifyAdd() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.add(vm6, 2, transition4)
        assertAdapterInvalidatedTo(vm6, transition4)
        assertContent(arrayOf(vm0, vm1, vm6, vm2, vm3, vm4), vm6)
    }

    @Test
    fun verifyAddRequireSameItem() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.add(vm6, 2, transition4, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM)
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm1, vm6, vm2, vm3, vm4), vm2)
    }

    @Test
    fun verifyReplaceList() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.replaceList(listOf(vm2, vm3, vm7, vm8), transition1)
        assertAdapterInvalidatedTo(vm7, transition1)
        assertContent(arrayOf(vm2, vm3, vm7, vm8), vm7)
        assertTerminated(vm0, vm1, vm4)
    }

    @Test
    fun verifyUpdateSelectConcrete() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = 3) {
            it.remove(vm1)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(vm4, transition3)
        assertContent(arrayOf(vm0, vm2, vm3, vm4, vm6), vm4)
        assertTerminated(vm1)
    }

    @Test
    fun verifyUpdateSelectSamePosition() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_POSITION) {
            it.remove(vm1)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(vm3, transition3)
        assertContent(arrayOf(vm0, vm2, vm3, vm4, vm6), vm3)
        assertTerminated(vm1)
    }

    @Test
    fun verifyUpdateSelectSameItem() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM) {
            it.remove(vm1)
            it.add(vm6)
        }
        assertAdapterNotChanged()
        assertContent(arrayOf(vm0, vm2, vm3, vm4, vm6), vm2)
        assertTerminated(vm1)
    }

    @Test
    fun verifyUpdateSelectSameWhenItRemoved() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = ISelectorNavigationModel.SELECTOR_SAME_ITEM) {
            it.remove(vm2)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(null, transition3)
        assertContent(arrayOf(vm0, vm1, vm3, vm4, vm6), null)
        assertTerminated(vm2)
    }

    @Test
    fun verifyUpdateSelectLast() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = ISelectorNavigationModel.SELECTOR_LAST) {
            it.remove(vm1)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(vm6, transition3)
        assertContent(arrayOf(vm0, vm2, vm3, vm4, vm6), vm6)
        assertTerminated(vm1)
    }

    @Test
    fun verifyUpdateSelectNothing() {
        initDefaults()
        navigator.setSelectorIndex(2, transition2)
        assertAdapterInvalidatedTo(vm2, transition2)

        navigator.update(transition3, selectorIndex = ISelectorNavigationModel.SELECTOR_NOTHING) {
            it.remove(vm1)
            it.add(vm6)
        }
        assertAdapterInvalidatedTo(null, transition3)
        assertContent(arrayOf(vm0, vm2, vm3, vm4, vm6), null)
        assertTerminated(vm1)
    }

    @Test
    fun verifySetSelectorIndexConcrete() {
        initDefaults()
        navigator.setSelectorIndex(3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)

        navigator.setSelectorIndex(3, transition3)
        assertAdapterNotChanged()
    }

    @Test
    fun verifySetSelectorIndexNothing() {
        initDefaults()
        navigator.setSelectorIndex(3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)

        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_NOTHING, transition3)
        assertAdapterInvalidatedTo(null, transition3)
    }

    @Test
    fun verifySetSelectorIndexSamePosition() {
        initDefaults()
        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_SAME_POSITION, transition1)
        assertAdapterNotChanged()

        navigator.setSelectorIndex(3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)

        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_SAME_POSITION, transition3)
        assertAdapterNotChanged()
    }

    @Test
    fun verifySetSelectorIndexSameItem() {
        initDefaults()
        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_SAME_ITEM, transition1)
        assertAdapterNotChanged()

        navigator.setSelectorIndex(3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)

        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_SAME_ITEM, transition3)
        assertAdapterNotChanged()
    }

    @Test
    fun verifySetSelectorIndexLast() {
        initDefaults()

        navigator.setSelectorIndex(3, transition2)
        assertAdapterInvalidatedTo(vm3, transition2)

        navigator.setSelectorIndex(ISelectorNavigationModel.SELECTOR_LAST, transition3)
        assertAdapterInvalidatedTo(vm4, transition3)
    }

}