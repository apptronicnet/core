package net.apptronic.core.mvvm.common

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.onSubscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

private fun IntRange.normalize(text: String): IntRange {
    return IntRange(
            start = when {
                start < 0 -> 0
                start > text.length -> text.length
                else -> start
            },
            endInclusive = when {
                endInclusive < start -> start
                endInclusive > text.length -> text.length
                else -> endInclusive
            }
    )
}

fun ViewModel.textInput(defaultValue: String = ""): TextInputViewModel {
    return TextInputViewModel(this).apply {
        setText(defaultValue)
    }
}

open class TextInputViewModel : ViewModel {

    constructor(context: ViewModelContext) : super(context)

    constructor(parent: ViewModel) : super(parent)

    internal val text = value<String>("")
    internal val selection = value<IntRange>(0..0)
    internal val inputUpdateRequest = typedEvent<InputUpdateRequest>()

    fun setText(text: String) {
        setText(text, text.length)
    }

    fun setText(text: String, selection: Int) {
        setText(text, selection..selection)
    }

    fun setText(text: String, selection: IntRange) {
        this.text.set(text)
        val realSelection = selection.normalize(text)
        this.selection.set(realSelection)
        inputUpdateRequest.sendEvent(InputUpdateRequest(text, realSelection))
    }

    fun text(): Entity<String> {
        return text
    }

    fun selection(): Entity<IntRange> {
        return selection
    }

    fun getBindingModel(): TextInputBindingModel {
        return TextInputBindingModel(this)
    }

    fun getText() : String {
        return text.getOr("")
    }

}

class InputUpdateRequest(
        val text: String,
        val selection: IntRange
)

class TextInputBindingModel internal constructor(
        private val target: TextInputViewModel
) {

    fun observeUpdates(): Entity<InputUpdateRequest> {
        return target.inputUpdateRequest.onSubscribe<InputUpdateRequest> {
                InputUpdateRequest(
                        text = target.text.getOr(""),
                        selection = target.selection.getOr(0..0)
                )
        }
    }

    fun onTextChanged(text: String) {
        target.text.set(text)
    }

    fun onSelectionChanged(selection: IntRange) {
        target.selection.set(selection.normalize(target.text.getOr("")))
    }

}