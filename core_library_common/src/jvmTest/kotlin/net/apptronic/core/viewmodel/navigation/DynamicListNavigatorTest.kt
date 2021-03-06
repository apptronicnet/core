package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.TestViewModel
import net.apptronic.core.viewmodel.ViewModel
import org.junit.Test

class DynamicListNavigatorTest : TestViewModel() {

    val lifecycleController = ViewModelLifecycleController(this)

    private fun enterAllStages() {
        lifecycleController.setAttached(true)
        lifecycleController.setBound(true)
        navigator.setAdapter(adapter)
        lifecycleController.setVisible(true)
        lifecycleController.setFocused(true)
    }


    sealed class Item {

        data class Static(val type: String) : Item()
        data class Dynamic(val id: Int) : Item()

    }

    private fun static(type: String): Item = Item.Static(type)
    private fun dynamic(id: Int): Item = Item.Dynamic(id)

    private val sources = value<List<Item>>(emptyList())
    private val statics = value<List<Item>>(emptyList())

    private val navigator = listDynamicNavigator(sources, StaticAdapter() + DynamicAdapter())

    val adapter = TestListAdapter()

    init {
        statics.subscribe {
            navigator.setStaticItems(it)
        }
    }

    open class ItemViewModel(context: Context) : ViewModel(context)
    class StaticItemViewModel(
        context: Context, val item: Item.Static
    ) : ItemViewModel(context), ViewModelWithVisibility {

        val isReadyToShow = value(false)

        override fun isReadyToShow(): Entity<Boolean> {
            // here it will be ignored as this item always dynamic
            return isReadyToShow
        }

    }

    class DynamicItemViewModel(
        context: Context, val item: Item.Dynamic
    ) : ItemViewModel(context), ViewModelWithVisibility {

        private val isReadyToShow = value(false)

        override fun isReadyToShow(): Entity<Boolean> {
            return isReadyToShow
        }

    }

    class StaticAdapter : ViewModelAdapter<Item.Static, String, StaticItemViewModel> {

        override fun getItemId(item: Item.Static): String {
            return "Static_" + item.type
        }

        override fun createViewModel(parent: Contextual, item: Item.Static): StaticItemViewModel {
            val context = parent.childContext()
            return StaticItemViewModel(context, item)
        }

    }

    class DynamicAdapter : ViewModelAdapter<Item.Dynamic, String, DynamicItemViewModel> {

        override fun getItemId(item: Item.Dynamic): String {
            return "Dynamic:" + item.id
        }

        override fun createViewModel(parent: Contextual, item: Item.Dynamic): DynamicItemViewModel {
            val context = parent.childContext()
            return DynamicItemViewModel(context, item)
        }

    }

    private fun assertStatus(
            allSize: Int = sources.get().size,
            hasHidden: Boolean = false,
            allItems: List<Any> = sources.get(),
            visibleItems: List<Any> = allItems,
            visibleSize: Int = visibleItems.size,
            staticItems: List<Any> = statics.get(),
            attachedViewModels: Set<Any>
    ) {
        val content = navigator.content.get()
        assert(allSize == content.countAll)
        assert(visibleSize == content.countVisible)
        assert(hasHidden == content.hasHidden)
        assert(allItems.toTypedArray().contentEquals(content.all.toTypedArray()))
        assert(visibleItems.toTypedArray().contentEquals(content.visible.toTypedArray()))
        assert(staticItems.toTypedArray().contentEquals(content.staticItems.toTypedArray()))
        assert(staticItems.toTypedArray().contentEquals(content.staticItems.toTypedArray()))
        assert(attachedViewModels.size == content.attachedViewModels.size)
        attachedViewModels.forEach { keyOrValue ->
            when (keyOrValue) {
                is ViewModelItem -> assert(content.attachedViewModels.contains(keyOrValue.viewModel))
                is String -> assert(content.attachedViewModels.any {
                    it is StaticItemViewModel && it.item == static(keyOrValue)
                })
                else -> throw IllegalArgumentException(keyOrValue.toString())
            }
        }
    }

    @Test
    fun shouldBuildItemsCorrectly() {
        navigator.setSavedItemsSize(1)
        sources.set(
                listOf(
                        dynamic(0),
                        dynamic(1),
                        dynamic(2),
                        dynamic(3),
                        dynamic(4),
                        dynamic(5)
                )
        )
        enterAllStages()
        assert(navigator.size == 6)
        assertStatus(attachedViewModels = emptySet())
        val vm1 = adapter.items[1]
        assert((vm1.viewModel as DynamicItemViewModel).item == dynamic(1))
        adapter.setFullBound(vm1, true)
        assert(vm1.viewModel.isStateFocused())
        assertStatus(attachedViewModels = setOf(vm1))

        val vm2 = adapter.items[2]
        assert((vm2.viewModel as DynamicItemViewModel).item == dynamic(2))
        adapter.setFullBound(vm2, true)
        assert(vm2.viewModel.isStateFocused())
        assertStatus(attachedViewModels = setOf(vm1, vm2))

        val vm3 = adapter.items[3]
        assert((vm3.viewModel as DynamicItemViewModel).item == dynamic(3))
        adapter.setFullBound(vm3, true)
        assert(vm3.viewModel.isStateFocused())
        assertStatus(attachedViewModels = setOf(vm1, vm2, vm3))

        adapter.setFullBound(vm1, false)
        assert(vm1.viewModel.isStateAttached()) // retained 1 value
        assertStatus(attachedViewModels = setOf(vm1, vm2, vm3))

        adapter.setFullBound(vm2, false)
        assert(vm2.viewModel.isStateAttached()) // retained 1 value
        assert(vm1.viewModel.isTerminated()) // but destroyed old value
        assertStatus(attachedViewModels = setOf(vm2, vm3))

        val vm1new = adapter.items[1]
        val vm2old = adapter.items[2]
        assert(vm1new != vm1)
        assert(vm2old == vm2)
        assert(vm2old.viewModel.isStateAttached())
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3))

        sources.set(
                listOf(
                        static("one"), // 0
                        static("two"), // 1
                        dynamic(0),      // 2
                        dynamic(1),      // 3
                        dynamic(2),      // 4
                        dynamic(3),      // 5
                        dynamic(4),      // 6
                        dynamic(5),      // 7
                        dynamic(6)       // 8
                )
        )
        statics.set(
                listOf(
                        static("one"),
                        static("two")
                )
        )
        assert(adapter.items.size == 9)
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, "one", "two"))

        val vm4 = adapter.items[6]
        assert((vm4.viewModel as DynamicItemViewModel).item == dynamic(4))
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, vm4, "one", "two"))

        val vm5 = adapter.items[7]
        assert((vm5.viewModel as DynamicItemViewModel).item == dynamic(5))
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, vm4, vm5, "one", "two"))

        val vm6 = adapter.items[8]
        assert((vm6.viewModel as DynamicItemViewModel).item == dynamic(6))
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, vm4, vm5, vm6, "one", "two"))

        val vmOne = adapter.items[0]
        assert((vmOne.viewModel as StaticItemViewModel).item == static("one"))
        adapter.setFullBound(vmOne, true)
        assert(vmOne.viewModel.isStateFocused())
        adapter.setFullBound(vmOne, false)
        assert(vmOne.viewModel.isStateAttached())
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, vm4, vm5, vm6, vmOne, "two"))

        val vmTwo = adapter.items[1]
        assert((vmTwo.viewModel as StaticItemViewModel).item == static("two"))
        assertStatus(attachedViewModels = setOf(vm1new, vm2, vm3, vm4, vm5, vm6, vmOne, vmTwo))

        adapter.setFullBound(vmOne, false)
        adapter.setFullBound(vmOne, false)
        adapter.setFullBound(vm2, false)
        adapter.setFullBound(vm4, false)
        adapter.setFullBound(vm5, false)
        adapter.setFullBound(vm6, false)

        assert(vm2.viewModel.isTerminated())
        assert(vm4.viewModel.isTerminated())
        assert(vm5.viewModel.isTerminated())
        assert(vm6.viewModel.isStateAttached())  // retained 1 value
        assert(vmOne.viewModel.isStateAttached())  // retained as static item
        assert(vmTwo.viewModel.isStateAttached())  // retained as static item
        assertStatus(attachedViewModels = setOf(vm1new, vm3, vm6, vmOne, vmTwo))

        val vm4new = adapter.items[6]
        assert(vm4new != vm4)
        val vm5new = adapter.items[7]
        assert(vm5new != vm5)
        val vm6old = adapter.items[8]
        assert(vm6old == vm6)
        assertStatus(attachedViewModels = setOf(vm1new, vm3, vm4new, vm5new, vm6, vmOne, vmTwo))

        adapter.setFullBound(vmOne, true)
        adapter.setFullBound(vmTwo, true)

        sources.set(
                listOf(
                        static("one"),  // 0
                        static("two"),  // 1
                        dynamic(0),       // 2
                        dynamic(1),       // 3
                        dynamic(2),       // 4
                        dynamic(3),       // 5
                        dynamic(4),       // 6
                        dynamic(5),       // 7
                        dynamic(6),      // 8
                        static("three") // 9
                )
        )
        statics.set(
                listOf(
                        static("one"),
                        static("three")
                )
        )
        assert(adapter.items.size == 10)
        assertStatus(attachedViewModels = setOf(vm1new, vm3, vm4new, vm5new, vm6old, vmOne, vmTwo, "three"))

        adapter.setFullBound(vmOne, false)
        adapter.setFullBound(vmTwo, false)
        adapter.setFullBound(vm4new, false)
        adapter.setFullBound(vm5new, false)
        adapter.setFullBound(vm6, false)

        assert(vmOne.viewModel.isStateAttached())
        assert(vmTwo.viewModel.isTerminated())
        assert(vm4new.viewModel.isTerminated())
        assert(vm5new.viewModel.isTerminated())
        assert(vm6.viewModel.isStateAttached())
        assertStatus(attachedViewModels = setOf(vm1new, vm3, vm6, vmOne, "three"))
    }

    private fun getStatic(type: String): StaticItemViewModel {
        return navigator.getViewModelForItem(static(type))!! as StaticItemViewModel
    }

    @Test
    fun shouldWorkWithFiltersCorrectly() {
        navigator.setSimpleVisibilityFilter()
        navigator.setListFilter(takeWhileVisibleStaticsOnStartFilter())
        navigator.setSavedItemsSize(0)
        sources.set(
                listOf(
                        dynamic(1),
                        dynamic(2),
                        dynamic(3)
                )
        )
        enterAllStages()
        assertStatus(attachedViewModels = emptySet())
        val vm1 = adapter.items[0]
        val vm2 = adapter.items[1]
        val vm3 = adapter.items[2]
        assertStatus(attachedViewModels = setOf(vm1, vm2, vm3))
        sources.set(
                listOf(
                        static("First"),
                        dynamic(1),
                        dynamic(2),
                        dynamic(3)
                )
        )
        // "First" is not static now
        assertStatus(attachedViewModels = setOf(vm1, vm2, vm3))
        // make "First" static
        statics.set(listOf(static("First")))
        adapter.setFullBound(vm1, false)
        adapter.setFullBound(vm2, false)
        adapter.setFullBound(vm3, false)
        assertStatus(
                visibleSize = 0,
                visibleItems = emptyList(),
                hasHidden = true,
                attachedViewModels = setOf("First"))
        getStatic("First").isReadyToShow.set(true)
        assertStatus(
                attachedViewModels = setOf("First"))
        val vm1new = adapter.items[1]
        val vm2new = adapter.items[2]
        val vm3new = adapter.items[3]
        assertStatus(
                attachedViewModels = setOf(vm1new, vm2new, vm3new, "First"))
    }

}