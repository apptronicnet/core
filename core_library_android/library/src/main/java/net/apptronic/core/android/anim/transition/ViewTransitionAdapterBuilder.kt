package net.apptronic.core.android.anim.transition

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.anim.ViewAnimationDefinition
import net.apptronic.core.android.anim.ViewTransformationBuilder
import net.apptronic.core.android.anim.viewAnimation

class KeyDurationBuilder internal constructor(
    private val key: Any?,
    private val durationMap: MutableMap<Any?, Long>,
    private val durationMultiplierMap: MutableMap<Any?, Float>
) {

    fun duration(duration: Long): KeyDurationBuilder {
        durationMap[key] = duration
        return this
    }

    fun durationMultiplier(duration: Float): KeyDurationBuilder {
        durationMultiplierMap[key] = duration
        return this
    }

}

class ViewTransitionAdapterBuilder internal constructor() {

    internal val transitionDefinitions = mutableMapOf<Any?, ViewTransitionDefinition>()
    internal val transitionDurations = mutableMapOf<Any?, Long>()
    internal val transitionDurationMultipliers = mutableMapOf<Any?, Float>()

    internal val enterDefinitions = mutableMapOf<Any?, ViewAnimationDefinition>()
    internal val enterDurations = mutableMapOf<Any?, Long>()
    internal val enterDurationMultipliers = mutableMapOf<Any?, Float>()

    internal val exitDefinitions = mutableMapOf<Any?, ViewAnimationDefinition>()
    internal val exitDurations = mutableMapOf<Any?, Long>()
    internal val exitDurationMultipliers = mutableMapOf<Any?, Float>()

    private fun transitionKeyBuilder(key: Any?): KeyDurationBuilder {
        return KeyDurationBuilder(key, transitionDurations, transitionDurationMultipliers)
    }

    private fun enterKeyBuilder(key: Any?): KeyDurationBuilder {
        return KeyDurationBuilder(key, enterDurations, enterDurationMultipliers)
    }

    private fun exitKeyBuilder(key: Any?): KeyDurationBuilder {
        return KeyDurationBuilder(key, exitDurations, exitDurationMultipliers)
    }

    fun bindEnterAnimation(
        key: Any?,
        enter: ViewAnimationDefinition
    ): KeyDurationBuilder {
        enterDefinitions[key] = enter
        return enterKeyBuilder(key)
    }

    fun bindEnterAnimation(
        key: Any?,
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ): KeyDurationBuilder {
        val definition = viewAnimation(interpolator, buildFlow)
        return bindEnterAnimation(key, definition)
    }

    fun bindExitAnimation(
        key: Any?,
        exit: ViewAnimationDefinition
    ): KeyDurationBuilder {
        exitDefinitions[key] = exit
        return exitKeyBuilder(key)
    }

    fun bindExitAnimation(
        key: Any?,
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: ViewTransformationBuilder.() -> Unit
    ): KeyDurationBuilder {
        val definition = viewAnimation(interpolator, buildFlow)
        return bindExitAnimation(key, definition)
    }

    fun bindTransition(
        key: Any?,
        definition: ViewTransitionDefinition
    ): KeyDurationBuilder {
        transitionDefinitions[key] = definition
        return transitionKeyBuilder(key)
    }

    fun bindTransition(
        key: Any?, transitionBuildFlow: TransitionDefinitionBuilder.() -> Unit
    ): KeyDurationBuilder {
        val definition = viewTransition(transitionBuildFlow)
        return bindTransition(key, definition)
    }

}