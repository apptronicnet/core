package net.apptronic.core.viewmodel.commons

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.UpdateEntity
import net.apptronic.core.entity.behavior.onSubscribe
import net.apptronic.core.entity.typedEvent
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext

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

fun TextInputViewModel.withOnUpdate(target: UpdateEntity<String>): TextInputViewModel {
    observeTextUpdates().subscribe(target)
    return this
}

fun TextInputViewModel.withOnUpdate(callback: (String) -> Unit): TextInputViewModel {
    observeTextUpdates(callback)
    return this
}

fun IViewModel.textInput(defaultValue: String = ""): TextInputViewModel {
    return TextInputViewModel(this).apply {
        setText(defaultValue)
    }
}

class TextInputViewModel : ViewModel {

    constructor(context: ViewModelContext) : super(context)

    constructor(parent: IViewModel) : super(parent)

    internal val text = value<String>("")
    internal val textUpdates = typedEvent<String>()
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

    fun setSelection(selection: Int) {
        setSelection(selection..selection)
    }

    fun setSelection(selection: IntRange) {
        val realSelection = selection.normalize(getText())
        this.selection.set(realSelection)
        inputUpdateRequest.sendEvent(InputUpdateRequest(getText(), realSelection))
    }

    fun updateText(text: String) {
        if (this.text.get() != text) {
            this.text.set(text)
            textUpdates.sendEvent(text)
        }
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

    fun getText(): String {
        return text.getOr("")
    }

    fun observeTextUpdates(): Entity<String> {
        return textUpdates
    }

    fun observeTextUpdates(callback: (String) -> Unit) {
        textUpdates.subscribe(callback)
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
        target.updateText(text)
    }

    fun onSelectionChanged(selection: IntRange) {
        target.selection.set(selection.normalize(target.text.getOr("")))
    }

}