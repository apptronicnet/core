package net.apptronic.core.view.binder

import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.testutils.testContext
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.container.stackContainer
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.LayoutDirection
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.commons.ICoreButtonView
import net.apptronic.core.view.widgets.commons.ICoreTextView
import net.apptronic.core.view.widgets.textView

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


private class SampleDynamicViewBinder(context: CoreViewContext) : CoreDynamicViewBinder<SomeViewModel>(context) {

    lateinit var textView: ICoreTextView
    lateinit var upperCasedTextView: ICoreTextView
    lateinit var staticTextView: ICoreTextView
    lateinit var methodButton: ICoreButtonView
    lateinit var eventButton: ICoreButtonView

    override val view = stackContainer {
        orientation(Vertical)
        textView = textView {
            text(entity(SomeViewModel::text))
        }
        upperCasedTextView = textView {
            withViewModel { viewModel ->
                val text = viewModel.text.map { it.toUpperCase() }
                text(text)
            }
        }
        staticTextView = textView {
            text(member(SomeViewModel::staticText))
        }
        methodButton = buttonTextView {
            onClick(member { ::onClick })
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

    private val binder = SampleDynamicViewBinder(CoreViewContext(ViewConfiguration(LayoutDirection.LeftToRight)))

//    @Test
//    fun verifyFlow() {
//
//        val model1 = SomeViewModel(context.viewModelContext(), "st1")
//
//        binder.nextViewModel(model1)
//
//        model1.text.set("hello")
//        assertEquals(binder.textView.text.getValue(), "hello")
//        assertEquals(binder.upperCasedTextView.text.getValue(), "HELLO")
//
//        fail("Not completed")
//    }

}