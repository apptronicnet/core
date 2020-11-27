package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.value

private fun IntRange.normalize(text: String): IntRange {
    val startValue = when {
        start < 0 -> 0
        start > text.length -> text.length
        else -> start
    }
    val endValue = maxOf(startValue, when {
        endInclusive < start -> start
        endInclusive > text.length -> text.length
        else -> endInclusive
    })
    return IntRange(
            start = startValue,
            endInclusive = endValue
    )
}

fun Contextual.textInput(defaultValue: String = ""): TextInputModel {
    return TextInputModel(context).apply {
        setText(defaultValue)
    }
}

class TextInputModel internal constructor(
        context: Context,
        private val value: MutableValue<String> = BaseMutableValue(context)
) : MutableValue<String> by value {

    val selection = context.value<IntRange>(0..0)

    override fun set(value: String) {
        this.value.set(value)
        selection.set(value.length..value.length)
    }

    override fun update(value: String) {
        this.value.update(value)
        if (selection.get().last > value.length) {
            selection.set(selection.get().normalize(get()))
        }
    }

    fun setTextOnly(text: String) {
        this.value.set(text)
        selection.get().let {
            if (it.first > text.length || it.last > text.length) {
                selection.update(text.length..text.length)
            }
        }
    }

    fun setText(text: String) {
        setText(text, text.length)
    }

    fun setText(text: String, selection: Int) {
        setText(text, selection..selection)
    }

    fun setText(text: String, selection: IntRange) {
        this.value.set(text)
        this.selection.set(selection.normalize(text))
    }

    fun setSelection(selection: Int) {
        setSelection(selection..selection)
    }

    fun setSelection(selection: IntRange) {
        this.selection.set(selection.normalize(get()))
    }

}
