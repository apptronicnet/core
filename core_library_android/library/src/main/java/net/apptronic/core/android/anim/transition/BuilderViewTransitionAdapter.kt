package net.apptronic.core.android.anim.transition

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

fun viewTransitionAdapter(buildFlow: ViewTransitionAdapterBuilder.() -> Unit): ViewTransitionAdapter {
    val builder = ViewTransitionAdapterBuilder()
    builder.buildFlow()
    return BuilderViewTransitionAdapter(builder)
}

internal class BuilderViewTransitionAdapter internal constructor(
    private val builder: ViewTransitionAdapterBuilder
) : ViewTransitionAdapter {

    private fun buildSingleAnimationSet(
        target: View, container: View, duration: Long, transitionSpec: Any?,
        isEnter: Boolean
    ): ViewAnimationSet? {
        val definitions = if (isEnter) builder.enterDefinitions else builder.exitDefinitions
        val durations = if (isEnter) builder.enterDurations else builder.exitDurations
        val durationMultipliers = if (isEnter)
            builder.enterDurationMultipliers else builder.exitDurationMultipliers
        val definition = definitions[transitionSpec]
        return if (definition != null) {
            val targetDuration = durations[transitionSpec] ?: duration
            val multipliedDuration = durationMultipliers[transitionSpec]?.let {
                (targetDuration * it).toLong()
            } ?: targetDuration
            return ViewAnimationSet(multipliedDuration).also {
                it.addAnimation(definition, target, container)
            }
        } else null
    }

    override fun buildViewTransition(
        enter: View?,
        exit: View?,
        container: View,
        duration: Long,
        transitionSpec: Any?
    ): ViewAnimationSet? {
        if (enter != null && exit == null) {
            val set = buildSingleAnimationSet(enter, container, duration, transitionSpec, true)
            if (set != null) {
                return set
            }
        }
        if (exit != null && enter == null) {
            val set = buildSingleAnimationSet(exit, container, duration, transitionSpec, false)
            if (set != null) {
                return set
            }
        }
        val definition = builder.transitionDefinitions[transitionSpec]
        return if (definition != null) {
            val targetDuration = builder.transitionDurations[transitionSpec] ?: duration
            val multipliedDuration = builder.transitionDurationMultipliers[transitionSpec]?.let {
                (targetDuration * it).toLong()
            } ?: targetDuration
            definition.buildAnimationSet(enter, exit, container, multipliedDuration)
        } else null
    }

    override fun getOrder(transitionSpec: Any?): ViewTransitionOrder? {
        return builder.transitionDefinitions[transitionSpec]?.order
    }

}