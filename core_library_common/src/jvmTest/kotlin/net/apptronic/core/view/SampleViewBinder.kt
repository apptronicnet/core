package net.apptronic.core.view

import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.view.binder.CoreViewBinder
import net.apptronic.core.view.container.frameContainer
import net.apptronic.core.view.container.stackContainer
import net.apptronic.core.view.dimension.pixels
import net.apptronic.core.view.properties.*
import net.apptronic.core.view.shape.rectangleDrawable
import net.apptronic.core.view.widgets.CoreTextButtonView
import net.apptronic.core.view.widgets.CoreTextView
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.commons.ICoreTextView
import net.apptronic.core.view.widgets.textView

class SampleViewModel(context: ViewModelContext) : ViewModel(context) {

    val text = value("Hello!")

    fun onClick() {
        // do some work
    }

    val timerValue = value<Int>()

}

val DefaultTextStyle = viewStyle<ICoreTextView> {
    textSize(16)
    textColor(CoreColor.rgbHex(0x8032ff))
}

val AppTheme = viewTheme {
    common {
        width(FitToContent)
        height(FitToContent)
    }
    typed<CoreTextView>(DefaultTextStyle)
    typed<CoreTextButtonView> {
        textSize(32)
        padding(horizontal = 24, vertical = 8)
    }
}

class SampleViewBinder : CoreViewBinder<SampleViewModel>() {

    override fun onBind(viewModel: SampleViewModel) {
        frameContainer {
            theme(AppTheme)
            background(White)
            padding(32)
            width(FitToParent)
            height(FitToParent)
            shadow(8)
            stackContainer {
                orientation(Vertical)
                size(width = FitToContent, height = FitToContent)
                contentAlignment(ToStart, ToTop)
                layoutAlignment(ToCenter, ToCenter)
                textView {
                    padding(horizontal = 30, vertical = 12.pixels)
                    text(viewModel.text)
                    textSize(16)
                }
                spacerView(8)
                buttonTextView {
                    style(DefaultTextStyle)
                    text("Hello")
                    onClick(viewModel::onClick)
                    indent(16, end = 32)
                    background(rectangleDrawable {
                        fillColor = Red
                        corners(all = 8)
                    })
                }
                spacerView(12)
                wrapperView {
                    size(64, 64)
                    transitionSpec(BasicTransition.Fade)
                    content(viewModel.timerValue) { timerTick ->
                        textView {
                            textSize(16)
                            textColor(Red)
                            text(timerTick.toString())
                        }
                    }
                }
            }
        }
    }

}