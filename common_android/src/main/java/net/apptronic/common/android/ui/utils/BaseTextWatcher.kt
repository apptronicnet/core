package net.apptronic.common.android.ui.utils

import android.text.Editable
import android.text.TextWatcher

open class BaseTextWatcher : TextWatcher {

    open override fun afterTextChanged(s: Editable?) {
        // stub
    }

    open override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // stub
    }

    open override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // stub
    }

}