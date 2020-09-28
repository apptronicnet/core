package net.apptronic.core.mvvm

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.terminate
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator
import net.apptronic.core.testutils.testContext
import org.junit.Test

class SubModelLifecycleTest {

    private val baseContext = testContext()

    private inner class ParentModel : LifecycleTestViewModel(EmptyViewModelContext.createContext(baseContext)) {

        val children = stackNavigator()

        private val adapter = SampleViewModelAdapter()

        init {
            children.setAdapter(adapter)
        }

        fun currentChild(): IViewModel? {
            return adapter.actualModel
        }

    }

    private class ChildModel(root: IViewModel) : LifecycleTestViewModel(root.viewModelContext()) {

        val children = stackNavigator()

        private val adapter = SampleViewModelAdapter()

        init {
            children.setAdapter(adapter)
        }

        fun currentChild(): IViewModel? {
            return adapter.actualModel
        }

    }

    private val root = ParentModel()
    private val controller =
            ViewModelLifecycleController(root)

    @Test
    fun shouldRunLifecycle() {

        controller.setAttached(true)
        assert(root.isAttached())

        controller.setBound(true)
        assert(root.isBound())

        controller.setVisible(true)
        assert(root.isVisible())

        controller.setFocused(true)
        assert(root.isFocused())

        controller.setAttached(false)
        assert(root.isAttached().not())

        controller.setBound(false)
        assert(root.isBound().not())

        controller.setVisible(false)
        assert(root.isVisible().not())

        controller.setFocused(false)
        assert(root.isFocused().not())
    }

    @Test
    fun shouldRunChildLifecycle() {
        val child = ChildModel(root)
        root.children.add(child)
        assert(root.currentChild() === child)
        assert(child.isAttached())

        assert(child.isAttached())

        controller.setBound(true)
        assert(child.isBound())

        controller.setVisible(true)
        assert(child.isVisible())

        controller.setFocused(true)
        assert(child.isFocused())

        controller.setAttached(false)
        assert(child.isAttached().not())

        controller.setBound(false)
        assert(child.isBound().not())

        controller.setVisible(false)
        assert(child.isVisible().not())

        controller.setFocused(false)
        assert(child.isFocused().not())
    }

    @Test
    fun shouldSwitchChildLifecycle() {
        val child1 = ChildModel(root)
        root.children.add(child1)
        assert(root.currentChild() == child1)

        controller.setAttached(true)
        assert(child1.isStateAttached())

        val child2 = ChildModel(root)
        root.children.add(child2)
        assert(root.currentChild() == child2)
        assert(child1.isStateAttached())
        assert(child2.isStateAttached())

        controller.setBound(true)
        assert(child1.isStateAttached())
        assert(child2.isStateBound())

        val child3 = ChildModel(root)
        root.children.add(child3)
        assert(root.currentChild() == child3)
        assert(child1.isStateAttached())
        assert(child2.isStateAttached())
        assert(child3.isStateBound())

        controller.setVisible(true)
        controller.setFocused(true)

        assert(child1.isStateAttached())
        assert(child2.isStateAttached())
        assert(child3.isStateFocused())

        val child4 = ChildModel(root)
        root.children.add(child4)
        assert(root.currentChild() == child4)

        assert(child1.isStateAttached())
        assert(child2.isStateAttached())
        assert(child3.isStateAttached())
        assert(child4.isStateFocused())

        root.children.popBackStack()
        assert(root.currentChild() == child3)
        assert(child1.isStateAttached())
        assert(child2.isStateAttached())
        assert(child3.isStateFocused())
        assert(child4.isTerminated())

        root.children.popBackStack()
        assert(root.currentChild() == child2)
        assert(child1.isStateAttached())
        assert(child2.isStateFocused())
        assert(child3.isTerminated())
        assert(child4.isTerminated())

        controller.setFocused(false)
        assert(child1.isStateAttached())
        assert(child2.isStateVisible())
        assert(child3.isTerminated())
        assert(child4.isTerminated())

        val child5 = ChildModel(root)
        root.children.replace(child5)
        assert(root.currentChild() == child5)
        assert(child1.isStateAttached())
        assert(child2.isTerminated())
        assert(child3.isTerminated())
        assert(child4.isTerminated())
        assert(child5.isStateVisible())

        controller.setVisible(false)
        controller.setBound(false)
        assert(child1.isStateAttached())
        assert(child2.isTerminated())
        assert(child3.isTerminated())
        assert(child4.isTerminated())
        assert(child5.isStateAttached())

        root.terminate()
        assert(child1.isTerminated())
        assert(child2.isTerminated())
        assert(child3.isTerminated())
        assert(child4.isTerminated())
        assert(child5.isTerminated())
    }

}