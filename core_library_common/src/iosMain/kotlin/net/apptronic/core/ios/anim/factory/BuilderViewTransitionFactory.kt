package net.apptronic.core.ios.anim.factory

import net.apptronic.core.ios.anim.ViewAnimation
import net.apptronic.core.ios.anim.transition.ViewTransition
import net.apptronic.core.ios.anim.transition.ViewTransitionFactoryBuilder

fun viewTransitionFactory(buildFlow: ViewTransitionFactoryBuilder.() -> Unit): ViewTransitionFactory {
    val builder = ViewTransitionFactoryBuilder()
    builder.buildFlow()
    return BuilderViewTransitionFactory(builder)
}

internal class BuilderViewTransitionFactory internal constructor(
        private val builder: ViewTransitionFactoryBuilder
) : ViewTransitionFactory {

    private fun buildSingleAnimation(
            spec: SingleTransitionSpec, isEnter: Boolean
    ): ViewAnimation? {
        val definitions = if (isEnter) builder.enterDefinitions else builder.exitDefinitions
        val durations = if (isEnter) builder.enterDurations else builder.exitDurations
        val durationMultipliers = if (isEnter)
            builder.enterDurationMultipliers else builder.exitDurationMultipliers
        val definition = definitions[spec.transitionSpec]
        return if (definition != null) {
            val targetDuration = durations[spec.transitionSpec] ?: spec.duration
            val multipliedDuration = durationMultipliers[spec.transitionSpec]?.let {
                (targetDuration * it).toLong()
            } ?: targetDuration
            definition.createAnimation(spec.target, spec.container, multipliedDuration)
        } else null
    }

    override fun buildViewTransition(spec: ViewTransitionSpec): ViewTransition? {
        val definition = builder.transitionDefinitions[spec.transitionSpec]
        return definition?.buildTransition(
                spec.enter,
                spec.exit,
                spec.container,
                getTransitionDuration(spec.transitionSpec, spec.duration),
                spec.direction
        )
    }

    private fun getTransitionDuration(transitionSpec: Any?, duration: Long): Long {
        val targetDuration = builder.transitionDurations[transitionSpec] ?: duration
        return builder.transitionDurationMultipliers[transitionSpec]?.let {
            (targetDuration * it).toLong()
        } ?: targetDuration
    }

    override fun buildSingleEnter(spec: SingleTransitionSpec): ViewAnimation? {
        return buildSingleAnimation(spec, true)
                ?: builder.transitionDefinitions[spec.transitionSpec]?.buildSingleEnter(
                        spec.target,
                        spec.container,
                        getTransitionDuration(spec.transitionSpec, spec.duration)
                )
    }

    override fun buildSingleExit(spec: SingleTransitionSpec): ViewAnimation? {
        return buildSingleAnimation(spec, false)
                ?: builder.transitionDefinitions[spec.transitionSpec]?.buildSingleExit(
                        spec.target,
                        spec.container,
                        getTransitionDuration(spec.transitionSpec, spec.duration)
                )
    }

}