package net.apptronic.core.android.anim

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.reverse

class ReversedTransformation internal constructor(
    private val transformation: ViewTransformation
) : ViewTransformation by transformation {

    override fun onTransform(target: View, container: View, progress: Progress) {
        transformation.onTransform(target, container, progress.reverse())
    }

}