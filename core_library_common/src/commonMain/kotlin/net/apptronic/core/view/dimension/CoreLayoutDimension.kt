package net.apptronic.core.view.dimension

interface CoreLayoutDimension

sealed class CoreLayoutSpec : CoreLayoutDimension {

    object FitToParent : CoreLayoutSpec() {
        override fun toString(): String = "FitToParent"
    }

    object FitToContent : CoreLayoutSpec() {
        override fun toString(): String = "FitToContent"
    }

}