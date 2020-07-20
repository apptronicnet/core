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

    fun getRunningTransformations(): Map<TransformationDescriptor, Transformation> {
        return transformationsList.associateBy {
            it.descriptor
        }
    }

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        transformations.forEach {
            it.onStart(target, container)
        }
        transformationsList.addAll(transformations)
        val keys = transformations.map { it.descriptor }
        if (interceptedTransition is TransformationTransition) {
            interceptedTransition.transformations.filterNot {
                keys.contains(it.descriptor)
            }.forEach {
                transformationsList.add(it.onCancel(target, container))
            }
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

