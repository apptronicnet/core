package net.apptronic.core.android.viewmodel.anim

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.transformation.TransformationDescriptor

interface ViewTransformation {

    /**
     * Descriptor identifying parameter which is modified by this [ViewTransformation]
     */
    val descriptor: TransformationDescriptor

    /**
     * Initialize starting state of [target] view inside of [container]
     */
    fun onStart(target: View, container: View)

    /**
     * Read existing state of [target] view inside of [container] as start state
     */
    fun onIntercept(target: View, container: View)

    /**
     * Apply transformation to [target] view inside of [container] according to [progress].
     *
     * For [progress] = 0 there should be start transformation
     *
     * For [progress] = 1 there should be end transformation
     */
    fun onTransform(target: View, container: View, progress: Progress)

    /**
     * Reset state of [target] view inside of [container] to untransformed state
     */
    fun onReset(target: View, container: View)

    fun onCancel(target: View, container: View): ViewTransformation

}