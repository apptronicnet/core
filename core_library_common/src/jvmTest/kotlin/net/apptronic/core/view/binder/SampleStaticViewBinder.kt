package net.apptronic.core.view.binder

import net.apptronic.core.entity.commons.value
import net.apptronic.core.view.*
import net.apptronic.core.view.commons.ICoreTextView
import net.apptronic.core.view.containers.frameContainer
import net.apptronic.core.view.containers.stackContainer
import net.apptronic.core.view.properties.ColorRed
import net.apptronic.core.view.properties.ColorWhite
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.rectangleShape
import net.apptronic.core.view.widgets.CoreTextButtonView
import net.apptronic.core.view.widgets.CoreTextView
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.textView
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.BasicTransition

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

class SampleStaticViewBinder(val viewModel: SampleViewModel) : ViewModelBinder<SampleViewModel>() {

    override fun view(): ICoreView = frameContainer {
        theme(AppTheme)
        rectangleShape {
            size(FitToParent, FitToParent)
            fillColor(ColorWhite)
        }
        padding(32)
        width(FitToParent); height(FitToParent)
        shadow(8)
        stackContainer {
            orientation(Vertical); size(width = FitToContent, height = FitToContent)
            contentAlignment(ToStart, ToTop); layoutAlignment(ToCenter, ToCenter)
            textView {
                padding(horizontal = 30, vertical = 12.pixels)
                text(viewModel.text)
                textSize(16)
            }
            spacerView(8)
            frameView {
                background {
                    rectangleShape {
                        fillColor(ColorRed)
                        corners(all = 8)
                    }
                }
                content {
                    buttonTextView {
                        style(DefaultTextStyle)
                        text("Hello")
                        onClick(viewModel::onClick)
                        indent(16, end = 32)
                    }
                }
            }
            spacerView(12)
            dynamicContentView {
                size(64, 64)
                transitionSpec(BasicTransition.Fade)
                content(viewModel.timerValue) { timerTick ->
                    textView {
                        textSize(16)
                        textColor(ColorRed)
                        text(timerTick.toString())
                    }
                }
            }
        }
    }


}