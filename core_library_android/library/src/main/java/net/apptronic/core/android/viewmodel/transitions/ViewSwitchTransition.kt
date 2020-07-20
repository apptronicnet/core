package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class ViewSwitchTransition(
    private val enterTransition: Transition<View>?,
    private val exitTransition: Transition<View>?
) : Transition<ViewSwitch>() {

    override fun clearRunningTransition(target: ViewSwitch) {
        target.entering?.let {
            enterTransition?.clearRunningTransition(it)
        }
        target.exiting?.let {
            exitTransition?.clearRunningTransition(it)
        }
    }

    override fun getRunningTransition(target: ViewSwitch): Transition<ViewSwitch>? {
        val enter = target.entering?.let {
            enterTransition?.getRunningTransition(target.entering)
        }
        val exit = target.exiting?.let {
            enterTransition?.getRunningTransition(target.exiting)
        }
        return if (enter != null || exit != null) {
            ViewSwitchTransition(enter, exit)
        } else null
    }

    override fun isAllowsTransition(target: ViewSwitch): Boolean {
        return listOfNotNull(
            target.entering?.let { enterTransition?.isAllowsTransition(it) },
            target.exiting?.let { exitTransition?.isAllowsTransition(it) }
        ).any { it }
    }

    override fun setRunningTransition(target: ViewSwitch) {
        target.entering?.let {
            enterTransition?.setRunningTransition(it)
        }
        target.exiting?.let {
            exitTransition?.setRunningTransition(it)
        }
    }

    override fun onStartTransition(
        target: ViewSwitch, interceptedTransition: Transition<ViewSwitch>?
    ) {
        target.entering?.let {
            enterTransition?.onStartTransition(
                it, (interceptedTransition as? ViewSwitchTransition)?.enterTransition
            )
        }
        target.exiting?.let {
            exitTransition?.onStartTransition(
                it, (interceptedTransition as? ViewSwitchTransition)?.exitTransition
            )
        }
    }

    override fun onApplyTransition(target: ViewSwitch, progress: Progress) {
        target.entering?.let {
            enterTransition?.onApplyTransition(
                it, progress.interpolateWith(enterTransition.interpolator)
            )
        }
        target.exiting?.let {
            exitTransition?.onApplyTransition(
                it, progress.interpolateWith(exitTransition.interpolator)
            )
        }
    }

    override fun onCompleteTransition(target: ViewSwitch, isCompleted: Boolean) {
        target.entering?.let {
            enterTransition?.onCompleteTransition(it, isCompleted)
        }
        target.exiting?.let {
            exitTransition?.onCompleteTransition(it, isCompleted)
        }
    }

}