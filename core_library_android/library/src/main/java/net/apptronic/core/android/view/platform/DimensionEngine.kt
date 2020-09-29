package net.apptronic.core.android.view.platform

import net.apptronic.core.view.dimension.CoreDimension

interface DimensionEngine {

    fun getDimensionPixelSizeInt(coreDimension: CoreDimension): Int

    fun getDimensionPixelSizeFloat(coreDimension: CoreDimension): Float

}