package net.apptronic.core.view.binder

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.testutils.testContext
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.stackContainer
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.LayoutDirection
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.commons.ICoreButtonView
import net.apptronic.core.view.widgets.commons.ICoreTextView
import net.apptronic.core.view.widgets.textView
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

private class SomeViewModel(context: ViewModelContext, val staticText: String) : ViewModel(context) {

    var onClickCount = 0
    var onClickEventCount = 0

    val text = value("")

    fun onClick() {
        onClickCount++
    }

    val onClickEvent = genericEvent {
        onClickEventCount++
    }

}


private class SampleDynamicBinderView(context: CoreViewContext) : CoreDynamicBinderView<SomeViewModel>(context) {

    val upperCased = value<String>().withViewModel { setAs(it.text.map { it.toUpperCase() }) }

    lateinit var textView: ICoreTextView
    lateinit var upperCasedTextView: ICoreTextView
    lateinit var staticTextView: ICoreTextView
    lateinit var methodButton: ICoreButtonView
    lateinit var eventButton: ICoreButtonView

    override fun view(): ICoreView = stackContainer {
        orientation(Vertical)
        textView = textView {
            withViewModel {
                text(it.text)
            }
        }
        upperCasedTextView = textView {
            withViewModel { viewModel ->
                val text = viewModel.text.map { it.toUpperCase() }
                text(text)
            }
        }
        staticTextView = textView {
            withViewModel {
                text(it.staticText)
            }
        }
        methodButton = buttonTextView {
            withViewModel {
                onClick(it::onClick)
            }
        }
        eventButton = buttonTextView {
            withViewModel {
                onClick(it.onClickEvent)
            }
        }
    }

}

class SimpleReusableViewBinderTest {

    private val context = testContext()

    private val binder = SampleDynamicBinderView(CoreViewContext(ViewConfiguration(LayoutDirection.LeftToRight)))

    @Test
    fun verifyFlow() {

        val model1 = SomeViewModel(context.viewModelContext(), "st1")

        binder.view()
        binder.nextViewModel(model1)

        model1.text.set("hello")
        assertEquals(binder.textView.text.get(), "hello")
        assertEquals(binder.upperCasedTextView.text.get(), "HELLO")

        fail("Not completed")
    }

}