package net.apptronic.core.view.binder

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.testutils.testContext
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.stackContainer
import net.apptronic.core.view.properties.LayoutDirection
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.commons.ICoreButtonView
import net.apptronic.core.view.widgets.commons.ICoreTextView
import net.apptronic.core.view.widgets.textView
import org.junit.Test
import kotlin.test.assertEquals

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


private class SampleReusableViewBinder : ReusableCoreViewBinder<SomeViewModel>() {

    val upperCased = readObservable(SomeViewModel::text) {
        it.toUpperCase()
    }

    lateinit var textView: ICoreTextView
    lateinit var upperCasedTextView: ICoreTextView
    lateinit var staticTextView: ICoreTextView
    lateinit var methodButton: ICoreButtonView
    lateinit var eventButton: ICoreButtonView

    override fun onCreateView() {
        stackContainer {
            orientation(Vertical)
            textView = textView {
                textFrom(SomeViewModel::text)
            }
            upperCasedTextView = textView {
                text(upperCased)
            }
            staticTextView = textView {
                text(SomeViewModel::staticText)
            }
            methodButton = buttonTextView {
                onClick(SomeViewModel::onClick)
            }
            eventButton = buttonTextView {
                onClickTo(SomeViewModel::onClickEvent)
            }
        }
    }

}

class SimpleReusableViewBinderTest {

    private val context = testContext()

    private val binder = SampleReusableViewBinder()

    @Test
    fun verifyFlow() {

        val model1 = SomeViewModel(context.viewModelContext(), "st1")
        binder.setConfiguration(ViewConfiguration(LayoutDirection.LeftToRight))

        binder.performCreateCoreView()
        binder.setViewModel(model1)

        model1.text.set("hello")
        assertEquals(binder.textView.text.get(), "hello")
        assertEquals(binder.upperCasedTextView.text.get(), "HELLO")
    }

}