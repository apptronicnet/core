package net.apptronic.core.view.dimension

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.ViewProperty

interface CoreLayoutDimension

object FitToParentDimension : AbstractDimensionNumber(-1f), CoreLayoutDimension {
    override fun toString(): String = "FitToParent"
}

object FitToContentDimension : AbstractDimensionNumber(-1f), CoreLayoutDimension {
    override fun toString(): String = "FitToContent"
}

fun Number.asCoreLayoutDimension(): CoreLayoutDimension {
    if (this is CoreLayoutDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}

fun ViewProperty<CoreLayoutDimension>.setAsCoreLayoutDimension(value: Number) {
    set(DiscreteCoreDimension(value.toFloat()))
}

fun ViewProperty<CoreLayoutDimension>.setAsCoreLayoutDimension(source: Observable<Number>) {
    set(source) {
        it.asCoreDimension()
    }
}