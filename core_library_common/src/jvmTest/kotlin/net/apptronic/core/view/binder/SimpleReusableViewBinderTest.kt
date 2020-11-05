package net.apptronic.core.view.binder

import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.testutils.createTestContext
import net.apptronic.core.view.commons.ICoreButtonView
import net.apptronic.core.view.commons.ICoreTextView
import net.apptronic.core.view.containers.stackContainer
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.viewTheme
import net.apptronic.core.view.widgets.CoreTextButtonView
import net.apptronic.core.view.widgets.CoreTextView
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.textView
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class SomeViewModel(context: ViewModelContext, val staticText: String) : ViewModel(context) {

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


class SampleDynamicViewBinder : ViewModelTypeBinder<SomeViewModel>() {

    lateinit var textView: ICoreTextView
    lateinit var upperCasedTextView: ICoreTextView
    lateinit var staticTextView: ICoreTextView
    lateinit var methodButton: ICoreButtonView
    lateinit var eventButton: ICoreButtonView

    override fun onBind(viewModel: SomeViewModel) {
        textView.text(viewModel.text)
        upperCasedTextView.text(viewModel.text.map { it.toUpperCase() })
        staticTextView.text(viewModel.staticText)
        methodButton.onClick(viewModel::onClick)
        eventButton.onClick(viewModel.onClickEvent)
    }

    private val TextColorDefault = CoreColor.rgbHex(0x272727)

    private val TextTheme = viewTheme {
        typed<CoreTextView> {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Medium)
        }
        typed<CoreTextButtonView> {
            textSize(15)
            textColor(CoreColor.rgbHex(0x545454))
            fontWeight(SemiBold)
        }
    }

    override fun view() = stackContainer {
        theme(TextTheme)
        orientation(Vertical)
        textView = textView {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Medium)
        }
        upperCasedTextView = textView {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Light)
        }
        staticTextView = textView {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Light)
        }
        methodButton = buttonTextView {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Light)
        }
        eventButton = buttonTextView {
            textSize(16)
            textColor(TextColorDefault)
            fontWeight(Light)
        }
    }

}

class SimpleReusableViewBinderTest {

    private val context = createTestContext()

    private val binder = SampleDynamicViewBinder()

    @Test
    fun verifyFlow() {

        val model1 = SomeViewModel(context.viewModelContext(), "st1")

        binder.view()
        binder.onBind(model1)

        model1.text.set("hello")
        assertEquals(binder.textView.text.get(), "hello")
        assertEquals(binder.upperCasedTextView.text.get(), "HELLO")

        fail("Not completed")
    }

}