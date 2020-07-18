package net.apptronic.core.android.viewmodel.transitions

class TransitionPlayer<Target>(
    private val target: Target,
    private val transition: Transition<Target>
) {

    private var isStarted = false

    fun seekTo(progress: Progress) {
        if (!isStarted) {
            transition.onStartTransition(target, null)
            isStarted = true
        }
        transition.onApplyTransition(target, progress)
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

    fun start() {
        if (!isStarted) {
            transition.onStartTransition(target, null)
            isStarted = true
        }
    }

    fun complete() {
        transition.onCompleteTransition(target, true)
    }

    fun cancel() {
        transition.onCompleteTransition(target, false)
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

    override fun onStartTransition(
        target: Transition<Target>, interceptedTransition: Transition<Transition<Target>>?
    ) {
        if (recallStart) {
            target.onStartTransition(targetOfTarget, null)
        }
    }

    override fun onApplyTransition(target: Transition<Target>, progress: Progress) {
        val targetProgress = progress.interpolate(startProgress, endProgress)
        target.onApplyTransition(targetOfTarget, targetProgress)
    }

    override fun onCompleteTransition(target: Transition<Target>, isCompleted: Boolean) {
        if (recallComplete) {
            target.onCompleteTransition(targetOfTarget, isCompleted)
        }
    }

}