package net.apptronic.core.view.dimension

sealed class CoreLayoutDimension {

    class SpecificDimension(val coreDimension: CoreDimension) : CoreLayoutDimension()

    object FitToParent : CoreLayoutDimension() {
        override fun toString(): String = "FitToParent"
    }

    object FitToContent : CoreLayoutDimension() {
        override fun toString(): String = "FitToContent"
    }

}

fun CoreLayoutDimension(number: Number): CoreLayoutDimension {
    return CoreLayoutDimension.SpecificDimension(number.asCoreDimension())
}