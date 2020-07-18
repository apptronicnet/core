package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import net.apptronic.core.android.R

abstract class ViewTransition : Transition<View>() {

    final override fun getRunningTransition(target: View): Transition<View>? {
        return target.getTag(R.id.TransitionAnimation) as? Transition<View>
    }

    final override fun setRunningTransition(target: View) {
        target.setTag(R.id.TransitionAnimation, this)
    }

    final override fun clearRunningTransition(target: View) {
        target.setTag(R.id.TransitionAnimation, null)
    }

    final override fun isAllowsTransition(target: View): Boolean {
        return target.handler != null
    }

    private var startVisibility: Int = 0
    private var isStartCalled = false
    private var interceptedTransition: Transition<View>? = null

    final override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        isStartCalled = false
        startVisibility = target.visibility
        if (target.width > 0 && target.height > 0) {
            startTransition(target, interceptedTransition)
            isStartCalled = true
            this.interceptedTransition = null
        } else {
            target.visibility = View.INVISIBLE
            this.interceptedTransition = interceptedTransition
        }
    }

    abstract fun startTransition(target: View, interceptedTransition: Transition<View>?)

    final override fun onApplyTransition(target: View, progress: Progress) {
        if (target.width > 0 && target.height > 0) {
            if (!isStartCalled) {
                target.visibility = startVisibility
                startTransition(target, interceptedTransition)
                isStartCalled = true
            }
            applyTransition(target, progress)
        }
    }

    abstract fun applyTransition(target: View, progress: Progress)

    final override fun onCompleteTransition(target: View, isCompleted: Boolean) {
        if (!isStartCalled) {
            target.visibility = startVisibility
        }
        interceptedTransition = null
        isStartCalled = false
        startVisibility = 0
        completeTransition(target, isCompleted)
    }

    abstract fun completeTransition(target: View, isCompleted: Boolean)


}