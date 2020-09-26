package net.apptronic.core.view

import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.view.binder.CoreViewBinder
import net.apptronic.core.view.container.frameView
import net.apptronic.core.view.container.stackView
import net.apptronic.core.view.dimension.pixels
import net.apptronic.core.view.properties.*
import net.apptronic.core.view.shape.rectangleDrawable
import net.apptronic.core.view.widgets.buttonTextView
import net.apptronic.core.view.widgets.textView

class SampleViewModel(context: ViewModelContext) : ViewModel(context) {

    val text = value("Hello!")

    fun onClick() {
        // do some work
    }

    val timerValue = value<Int>()

}

class SampleViewBinder : CoreViewBinder<SampleViewModel>() {

    override fun onBind(viewModel: SampleViewModel) {
        frameView {
            background(White)
            padding(32)
            width(FitToParent)
            height(FitToParent)
            shadow(8)
            stackView {
                orientation(Vertical)
                width(FitToContent)
                height(FitToContent)
                contentAlignment(ToStart, ToTop)
                layoutAlignment(ToCenter, ToCenter)
                textView {
                    padding(horizontal = 30, vertical = 12.pixels)
                    text(viewModel.text)
                    textSize(16)
                }
                spacerView(8)
                buttonTextView {
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
                    width(64)
                    height(64)
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