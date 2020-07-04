package net.apptronic.core.android.viewmodel.transitions

/**
 * This type of transition plays another transition from [startProgress] to [endProgress]
 * using interpolator from this transition itself
 */
class PlaybackTransition<Target>(
    private val targetOfTarget: Target,
    private val startProgress: Float,
    private val endProgress: Float
) : Transition<Transition<Target>>() {

    override fun clearRunningTransition(target: Transition<Target>) {
        target.clearRunningTransition(targetOfTarget)
    }

    override fun getRunningTransition(target: Transition<Target>): Transition<Transition<Target>>? {
        val targetRunning = target.getRunningTransition(targetOfTarget)
        return if (targetRunning != null) {
            PlaybackTransition(targetOfTarget, 0f, 1f)
        } else null
    }

    override fun isAllowsTransition(target: Transition<Target>): Boolean {
        return target.isAllowsTransition(targetOfTarget)
    }

    override fun setRunningTransition(target: Transition<Target>) {
        return target.setRunningTransition(targetOfTarget)
    }

    override fun onTransitionStarted(target: Transition<Target>) {
        target.onTransitionStarted(targetOfTarget)
    }

    override fun onTransitionIntercepted(target: Transition<Target>) {
        target.onTransitionIntercepted(targetOfTarget)
    }

    override fun applyTransition(target: Transition<Target>, progress: Progress) {
        val targetProgress = progress.interpolate(startProgress, endProgress)
        target.applyTransition(targetOfTarget, targetProgress)
    }

    override fun onTransitionCompleted(target: Transition<Target>) {
        target.onTransitionCompleted(targetOfTarget)
    }

    override fun onTransitionCancelled(target: Transition<Target>) {
        target.onTransitionCancelled(targetOfTarget)
    }

}