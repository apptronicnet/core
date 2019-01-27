package net.apptronic.common.core.mvvm

import android.view.View
import androidx.annotation.IdRes

fun <T : View> View.with(@IdRes id: Int, block: T.() -> Unit) {
    findViewById<T>(id)?.apply(block)
}