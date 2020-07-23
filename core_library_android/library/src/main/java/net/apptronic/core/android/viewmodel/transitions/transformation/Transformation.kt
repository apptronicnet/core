package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolateWith

abstract class Transformation {

    abstract val descriptor: TransformationDescriptor

    var interpolator: Interpolator? = null

    internal fun doTransformation(target: View, container: View, progress: Progress) {
        applyTransformation(target, container, progress.interpolateWith(interpolator))
    }

    abstract fun onStart(target: View, container: View, isIntercepted: Boolean)

    abstract fun applyTransformation(target: View, container: View, progress: Progress)

    abstract fun onCancel(target: View, container: View): Transformation

    abstract fun onClear(target: View)

    abstract fun reversed(): Transformation

}

