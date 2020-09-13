package net.apptronic.core.android.anim

import android.content.Context
import android.view.View

class TransformationBuilder internal constructor(val target: View, val container: View) {

    val context: Context = target.context

    private val transformations = mutableListOf<ViewTransformation>()

    fun add(transformation: ViewTransformation) {
        transformations.add(transformation)
    }

    internal fun set(): ViewTransformationSet {
        return ViewTransformationSet(transformations)
    }

}