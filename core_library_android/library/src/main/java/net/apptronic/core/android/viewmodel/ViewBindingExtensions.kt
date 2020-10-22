package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.android.R
import kotlin.reflect.KProperty

fun <T> View.getBinding(builder: (View) -> T): T {
    return getTag(R.id.ViewBinding) as? T
        ?: builder(this).also {
            setTag(R.id.ViewBinding, it)
        }
}

class ViewBindingProperty<E> internal constructor(
    private val viewBinder: ViewBinder<*>,
    private val builder: (View) -> E
) {

    private var reference: E? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): E {
        return reference ?: builder(viewBinder.view).also {
            reference = it
        }
    }

}