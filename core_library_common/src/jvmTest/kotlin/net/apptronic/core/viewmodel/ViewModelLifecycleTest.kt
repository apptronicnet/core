package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class ViewModelLifecycleTest {

    private val baseContext = createTestContext()

    private class SampleViewModel(context: Context) : ViewModel(context)

    private val viewModelContext = baseContext.childContext {
        ViewModelLifecycleDefinition.assignTo(this)
    }

    private val viewModel = SampleViewModel(viewModelContext)

    private class Counter {
        var onEnter = 0
        var onExitInside = 0
        var onExitOutside = 0
        fun assertEntered() {
            assert(onEnter == 1)
            assert(onExitInside == 0)
            assert(onExitOutside == 0)
        }

        fun assertExited() {
            assert(onEnter == 1)
            assert(onExitInside == 1)
            assert(onExitOutside == 1)
        }
    }

    @Test
    fun shouldExecuteCreated() {
        val counter = Counter()
        viewModel.doOnAttach {
            counter.onEnter++
            onExit {
                counter.onExitInside++
            }
        }
        viewModel.doOnDetach {
            counter.onExitOutside++
        }
        viewModelContext.setAttached(true)
        counter.assertEntered()
        viewModelContext.setAttached(false)
        counter.assertExited()
    }

    @Test
    fun shouldExecuteBound() {
        val counter = Counter()
        viewModel.doOnBind {
            counter.onEnter++
            onExit {
                counter.onExitInside++
            }
        }
        viewModel.doOnUnbind {
            counter.onExitOutside++
        }
        viewModelContext.setBound(true)
        counter.assertEntered()
        viewModelContext.setBound(false)
        counter.assertExited()
    }

    @Test
    fun shouldExecuteVisible() {
        val counter = Counter()
        viewModel.doOnVisible {
            counter.onEnter++
            onExit {
                counter.onExitInside++
            }
        }
        viewModel.doOnHidden {
            counter.onExitOutside++
        }
        viewModelContext.setVisible(true)
        counter.assertEntered()
        viewModelContext.setVisible(false)
        counter.assertExited()
    }

    @Test
    fun shouldExecuteFocused() {
        val counter = Counter()
        viewModel.doOnFocused {
            counter.onEnter++
            onExit {
                counter.onExitInside++
            }
        }
        viewModel.doOnUnfocused {
            counter.onExitOutside++
        }
        viewModelContext.setFocused(true)
        counter.assertEntered()
        viewModelContext.setFocused(false)
        counter.assertExited()
    }

}