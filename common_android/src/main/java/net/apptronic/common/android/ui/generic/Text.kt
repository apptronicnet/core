package net.apptronic.common.android.ui.generic

import android.content.Context
import androidx.annotation.StringRes

abstract class Text {

    companion object {
        fun ofValue(text: CharSequence): Text {
            return ValueText(text)
        }

        fun ofResource(@StringRes stringRes: Int): Text {
            return ResourceText(stringRes)
        }
    }

    abstract fun getValue(context: Context): CharSequence

    private class ValueText(private val value: CharSequence) : Text() {
        override fun getValue(context: Context): CharSequence {
            return value
        }
    }

    private class ResourceText(@StringRes private val stringRes: Int) : Text() {
        override fun getValue(context: Context): CharSequence {
            return context.resources.getText(stringRes)
        }
    }

}