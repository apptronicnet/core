package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class TransformationTransitionBuilder internal constructor(
    val target: View, val container: View
) {

    private val transformations = mutableListOf<Transformation>()

    fun add(t: Transformation, interpolator: Interpolator? = null) {
        transformations.add(
            t.also {
                it.interpolator = interpolator
            }
        )
    }

    var interpolator: Interpolator? = null

    internal fun build(): TransformationTransition {
        return TransformationTransition(container, transformations).also {
            val setInterpolator = interpolator
            if (setInterpolator != null) {
                it.interpolator = setInterpolator
            }
        }
    }

}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun transformationTransition(
    target: View, container: View? = null, builder: TransformationTransitionBuilder.() -> Unit
): TransformationTransition {
    return TransformationTransitionBuilder(target, container ?: target.parent as? View ?: target)
        .apply(builder)
        .build()
}