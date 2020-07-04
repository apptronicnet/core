package net.apptronic.core.android.viewmodel.transitions

class TransitionPlayer<Target>(
    private val target: Target,
    private val transition: Transition<Target>
) {

    private var isStarted = false

    fun seekTo(progress: Progress) {
        if (!isStarted) {
            transition.onTransitionStarted(target)
        }
        transition.applyTransition(target, progress)
    }

    fun play(
        start: Progress, end: Progress, duration: Long,
        recallComplete: Boolean,
        builder: Transition<*>.() -> Unit = {}
    ): Transition<*> {
        val recallStart = isStarted.not()
        return PlaybackTransition(target, start, end, recallStart, recallComplete)
            .withDuration(duration)
            .apply(builder)
            .launch(transition)
    }

    fun complete() {
        transition.onTransitionCompleted(target)
    }

    fun cancel() {
        transition.onTransitionCancelled(target)
    }

}

/**
 * This type of transition plays another transition from [startProgress] to [endProgress]
 * using interpolator from this transition itself
 */
private class PlaybackTransition<Target>(
    private val targetOfTarget: Target,
    private val startProgress: Float,
    private val endProgress: Float,
    private val recallStart: Boolean,
    private val recallComplete: Boolean
) : Transition<Transition<Target>>() {

    override fun clearRunningTransition(target: Transition<Target>) {
        target.clearRunningTransition(targetOfTarget)
    }

    override fun getRunningTransition(target: Transition<Target>): Transition<Transition<Target>>? {
        val targetRunning = target.getRunningTransition(targetOfTarget)
        return if (targetRunning != null) {
            PlaybackTransition(targetOfTarget, 0f, 1f, false, false)
        } else null
    }

    override fun isAllowsTransition(target: Transition<Target>): Boolean {
        return target.isAllowsTransition(targetOfTarget)
    }

    override fun setRunningTransition(target: Transition<Target>) {
        return target.setRunningTransition(targetOfTarget)
    }

    override fun onTransitionStarted(target: Transition<Target>) {
        if (recallStart) {
            target.onTransitionStarted(targetOfTarget)
        }
    }

    override fun onTransitionIntercepted(target: Transition<Target>) {
        if (recallStart) {
            target.onTransitionIntercepted(targetOfTarget)
        }
    }

    override fun applyTransition(target: Transition<Target>, progress: Progress) {
        val targetProgress = progress.interpolate(startProgress, endProgress)
        target.applyTransition(targetOfTarget, targetProgress)
    }

    override fun onTransitionCompleted(target: Transition<Target>) {
        if (recallComplete) {
            target.onTransitionCompleted(targetOfTarget)
        }
    }

    override fun onTransitionCancelled(target: Transition<Target>) {
        if (recallComplete) {
            target.onTransitionCancelled(targetOfTarget)
        }
    }

}