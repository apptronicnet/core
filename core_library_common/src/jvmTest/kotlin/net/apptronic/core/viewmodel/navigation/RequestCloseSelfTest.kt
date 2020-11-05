package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter
import net.apptronic.core.viewmodel.viewModelContext
import org.junit.After
import org.junit.Test
import kotlin.test.assertSame

class RequestCloseSelfTest {

    private val context = createTestContext()

    @After
    fun after() {
        context.terminate()
    }

    private val CloseSelfTranstion = Any()
    private val containerViewModel = ViewModel(context.viewModelContext())
    private val childViewModel = ViewModel(containerViewModel.viewModelContext())

    private var closeSelfCommand: Any? = null

    private fun verifyCloseSelf() {
        childViewModel.closeSelf(CloseSelfTranstion)
        assertSame(closeSelfCommand, CloseSelfTranstion)
    }

    @Test
    fun verifyRequestCloseSelfForStackNavigationModelSingleAdapter() {
        val navigator = containerViewModel.stackNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : SingleViewModelAdapter {
            override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
                closeSelfCommand = transitionInfo.spec
            }
        })
        verifyCloseSelf()
        assert(navigator.size == 0)
    }

    @Test
    fun verifyRequestCloseSelfForStackNavigationModelSingleListAdapter() {
        val navigator = containerViewModel.stackNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : SingleViewModelListAdapter {
            override fun onInvalidate(items: List<ViewModelItem>, visibleIndex: Int, transitionInfo: TransitionInfo) {
                closeSelfCommand = transitionInfo.spec
            }
        })
        verifyCloseSelf()
        assert(navigator.size == 0)
    }

    @Test
    fun verifyRequestCloseSelfForStackNavigationModelListAdapter() {
        val navigator = containerViewModel.stackNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : ViewModelListAdapter<Any> {
            override fun onDataChanged(items: List<ViewModelItem>, state: Any, updateSpec: Any?) {
                closeSelfCommand = updateSpec
            }
        })
        verifyCloseSelf()
        assert(navigator.size == 0)
    }

    @Test
    fun verifyRequestCloseSelfForSelectorNavigationModelSingleAdapter() {
        val navigator = containerViewModel.selectorNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : SingleViewModelAdapter {
            override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
                closeSelfCommand = transitionInfo.spec
            }
        })
        verifyCloseSelf()
        assert(navigator.items.isEmpty())
    }

    @Test
    fun verifyRequestCloseSelfForSelectorNavigationModelSingleListAdapter() {
        val navigator = containerViewModel.selectorNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : SingleViewModelListAdapter {
            override fun onInvalidate(items: List<ViewModelItem>, visibleIndex: Int, transitionInfo: TransitionInfo) {
                closeSelfCommand = transitionInfo.spec
            }
        })
        verifyCloseSelf()
        assert(navigator.items.isEmpty())
    }

    @Test
    fun verifyRequestCloseSelfForSelectorNavigationModelListAdapter() {
        val navigator = containerViewModel.selectorNavigator()
        navigator.set(childViewModel)
        navigator.setAdapter(object : ViewModelListAdapter<Any> {
            override fun onDataChanged(items: List<ViewModelItem>, state: Any, updateSpec: Any?) {
                closeSelfCommand = updateSpec
            }
        })
        verifyCloseSelf()
        assert(navigator.items.isEmpty())
    }

    @Test
    fun verifyRequestCloseSelfListNavigator() {
        val navigator = containerViewModel.listNavigator()
        navigator.set(listOf(childViewModel))
        navigator.setAdapter(object : ViewModelListAdapter<Any> {
            override fun onDataChanged(items: List<ViewModelItem>, state: Any, updateSpec: Any?) {
                closeSelfCommand = updateSpec
            }
        })
        verifyCloseSelf()
        assert(navigator.getViewModels().isEmpty())
    }

}