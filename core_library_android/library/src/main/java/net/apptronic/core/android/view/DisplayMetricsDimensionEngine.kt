package net.apptronic.core.android.view

import android.util.DisplayMetrics
import net.apptronic.core.android.view.platform.DimensionEngine
import net.apptronic.core.view.dimension.DiscreteCoreDimension

class DisplayMetricsDimensionEngine(
    private val displayMetrics: DisplayMetrics
) : DimensionEngine {

    override fun getDimensionPixelSizeFloat(dimension: Number): Float {
        return when (dimension) {
            is DiscreteCoreDimension -> dimension.size * displayMetrics.density
            else -> dimension.toFloat()
        }
    }

    override fun getDimensionPixelSizeInt(dimension: Number): Int {
        return when (dimension) {
            is DiscreteCoreDimension -> (dimension.size * displayMetrics.density).toInt()
            else -> dimension.toInt()
        }
    }

}