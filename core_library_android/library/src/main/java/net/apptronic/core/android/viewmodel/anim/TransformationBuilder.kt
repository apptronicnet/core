package net.apptronic.core.android.viewmodel.anim

class TransformationBuilder internal constructor() {

    private val transformations = mutableListOf<ViewTransformation>()

    fun add(transformation: ViewTransformation) {
        transformations.add(transformation)
    }

    internal fun set(): ViewTransformationSet {
        return ViewTransformationSet(transformations)
    }

}