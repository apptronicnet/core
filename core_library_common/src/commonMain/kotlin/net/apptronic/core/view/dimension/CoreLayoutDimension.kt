package net.apptronic.core.view.dimension

interface CoreLayoutDimension

object FitToParentDimension : AbstractDimensionNumber(-1f), CoreLayoutDimension {
    override fun toString(): String = "FitToParent"
}

object FitToContentDimension : AbstractDimensionNumber(-1f), CoreLayoutDimension {
    override fun toString(): String = "FitToContent"
}