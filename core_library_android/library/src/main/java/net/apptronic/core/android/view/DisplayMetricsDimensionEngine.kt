package net.apptronic.core.android.view

import android.util.DisplayMetrics
import net.apptronic.core.android.view.platform.DimensionEngine
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.DiscreteCoreDimension

class DisplayMetricsDimensionEngine(
        private val displayMetrics: DisplayMetrics
) : DimensionEngine {

    override fun getDimensionPixelSizeFloat(coreDimension: CoreDimension): Float {
        return when (coreDimension) {
            is DiscreteCoreDimension -> coreDimension.size * displayMetrics.density
            is Number -> coreDimension.toFloat()
            else -> 0f
        }
    }

    override fun getDimensionPixelSizeInt(coreDimension: CoreDimension): Int {
        return when (coreDimension) {
            is DiscreteCoreDimension -> (coreDimension.size * displayMetrics.density).toInt()
            is Number -> coreDimension.toInt()
            else -> 0
        }
    }

}