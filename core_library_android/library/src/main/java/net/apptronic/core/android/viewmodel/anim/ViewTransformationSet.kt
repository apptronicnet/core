package net.apptronic.core.android.viewmodel.anim

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress

internal class ViewTransformationSet(
    private val transformations: List<ViewTransformation>
) {

    private val transformationsToRun = mutableListOf<ViewTransformation>()

    fun start(target: View, container: View, intercepted: ViewTransformationSet?) {
        if (intercepted == null) {
            transformationsToRun.addAll(transformations)
            transformations.forEach {
                it.onStart(target, container)
                transformationsToRun.add(it)
            }
        } else {
            intercept(target, container, intercepted)
        }
    }

    private fun intercept(target: View, container: View, intercepted: ViewTransformationSet) {
        val interceptedDescriptors = intercepted.transformationsToRun.map { it.descriptor }.toSet()
        transformations.forEach {
            if (interceptedDescriptors.contains(it.descriptor)) {
                it.onIntercept(target, container)
            } else {
                it.onStart(target, container)
            }
        }
        transformationsToRun.addAll(transformations)
        val descriptors = transformationsToRun.map { it.descriptor }.toSet()
        intercepted.transformationsToRun.forEach {
            if (descriptors.contains(it.descriptor).not()) {
                val cancellation = it.cancelled(target, container)
                cancellation.onStart(target, container)
                transformationsToRun.add(cancellation)
            }
        }
    }

    fun transform(target: View, container: View, progress: Progress) {
        transformationsToRun.forEach {
            it.onTransform(target, container, progress)
        }
    }

    fun reset(target: View, container: View) {
        transformations.forEach {
            it.onReset(target, container)
        }
    }

    fun reversed(): ViewTransformationSet {
        return ViewTransformationSet(
            transformations.map {
                it.reversed()
            }
        )
    }

}