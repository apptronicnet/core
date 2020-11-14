package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.value

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

fun TextInputModel.withOnUpdate(target: SubjectEntity<String>): TextInputModel {
    subscribe(target)
    return this
}

fun TextInputModel.withOnUpdate(callback: (String) -> Unit): TextInputModel {
    subscribe(callback)
    return this
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

    private fun setOnlyText(value: String) {
        this.value.set(value)
    }

    override fun set(value: String) {
        this.value.set(value)
        if (selection.get().last > value.length) {
            selection.update(selection.get().normalize(get()))
        }
    }

    override fun update(value: String) {
        this.value.update(value)
        if (selection.get().last > value.length) {
            selection.update(selection.get().normalize(get()))
        }
    }

    fun setText(text: String) {
        setText(text, text.length)
    }

    fun setText(text: String, selection: Int) {
        setText(text, selection..selection)
    }

    fun setText(text: String, selection: IntRange) {
        setOnlyText(text)
        val realSelection = selection.normalize(text)
        this.selection.set(realSelection)
    }

    fun setSelection(selection: Int) {
        setSelection(selection..selection)
    }

    fun setSelection(selection: IntRange) {
        val realSelection = selection.normalize(get())
        this.selection.set(realSelection)
    }

}
