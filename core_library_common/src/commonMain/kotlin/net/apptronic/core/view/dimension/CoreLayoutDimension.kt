package net.apptronic.core.view.dimension

interface CoreLayoutDimension

class RelativeToParentDimension internal constructor(val relativeSize: Float) : CoreLayoutDimension

class FitToParentDimension internal constructor() : CoreLayoutDimension

class FitToContentDimension internal constructor() : CoreLayoutDimension

class ExactLayoutDimension internal constructor(val value: CoreDimension) : CoreLayoutDimension

fun exactLayoutDimension(value: Number): ExactLayoutDimension {
    return ExactLayoutDimension(value.asCoreDimension())
}

fun RelativeToParent(relativeSize: Float) = RelativeToParentDimension(relativeSize)