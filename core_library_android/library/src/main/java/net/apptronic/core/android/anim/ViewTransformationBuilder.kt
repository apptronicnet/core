package net.apptronic.core.android.anim

import android.content.Context
import android.view.View

class ViewTransformationBuilder internal constructor(
    val target: View,
    val container: View,
    val intercepted: Set<ViewTransformationDescriptor>
) {

    val context: Context = target.context

    private val transformations = mutableListOf<ViewTransformation>()

    fun isIntercepting(vararg descriptors: ViewTransformationDescriptor): Boolean {
        return descriptors.any {
            intercepted.contains(it)
        }
    }

    fun add(transformation: ViewTransformation) {
        transformations.add(transformation)
    }

    internal fun set(): ViewTransformationSet {
        return ViewTransformationSet(transformations)
    }

}