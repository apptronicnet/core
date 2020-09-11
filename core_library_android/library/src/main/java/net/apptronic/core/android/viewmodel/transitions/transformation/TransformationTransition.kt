package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.ViewTransition

class TransformationTransition(
    private val container: View,
    private val transformations: List<Transformation>
) : ViewTransition() {

    private val transformationsList = mutableListOf<Transformation>()

    fun reversed(): TransformationTransition {
        val reversedTransformations: List<Transformation> = transformations.map {
            val reversedTransformation = it.reversed()
            reversedTransformation.interpolator = it.interpolator?.reversed()
            reversedTransformation
        }
        return TransformationTransition(container, reversedTransformations)
    }

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        transformationsList.clear()
        val interceptedTransformationKeys = mutableListOf<TransformationDescriptor>()
        val cancellingTransformations = mutableListOf<Transformation>()
        val mainTransformationKeys = transformations.map { it.descriptor }
        if (interceptedTransition is TransformationTransition) {
            interceptedTransition.transformations.forEach {
                if (mainTransformationKeys.contains(it.descriptor)) {
                    interceptedTransformationKeys.add(it.descriptor)
                } else {
                    cancellingTransformations.add(it)
                }
            }
        }
        transformationsList.addAll(transformations)
        transformationsList.addAll(cancellingTransformations)
        transformationsList.forEach {
            it.onStart(target, container, interceptedTransformationKeys.contains(it.descriptor))
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        transformationsList.forEach {
            it.doTransformation(target, container, progress)
        }
    }

    override fun completeTransition(target: View, isCompleted: Boolean) {
        transformationsList.forEach {
            it.onClear(target)
        }
    }

}

