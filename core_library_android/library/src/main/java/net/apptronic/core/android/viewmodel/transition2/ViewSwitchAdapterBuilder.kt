package net.apptronic.core.android.viewmodel.transition2

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.anim.*

class ViewSwitchAdapterKeyBuilder internal constructor(
    private val key: Any?,
    private val durationMap: MutableMap<Any?, Long>,
    private val durationMultiplierMap: MutableMap<Any?, Float>
) {

    fun duration(duration: Long): ViewSwitchAdapterKeyBuilder {
        durationMap[key] = duration
        return this
    }

    fun durationMultiplier(duration: Float): ViewSwitchAdapterKeyBuilder {
        durationMultiplierMap[key] = duration
        return this
    }

}

class ViewSwitchAdapterBuilder internal constructor() {

    internal var definitions = mutableMapOf<Any?, ViewSwitchDefinition>()
    internal var durations = mutableMapOf<Any?, Long>()
    internal var durationMultipliers = mutableMapOf<Any?, Float>()

    private fun keyBuilder(key: Any?): ViewSwitchAdapterKeyBuilder {
        return ViewSwitchAdapterKeyBuilder(key, durations, durationMultipliers)
    }

    fun bindAnimations(
        key: Any?,
        enter: ViewAnimationDefinition,
        exit: ViewAnimationDefinition
    ): ViewSwitchAdapterKeyBuilder {
        definitions[key] = ViewSwitchDefinition(enter, exit)
        return keyBuilder(key)
    }

    fun bindEnterAnimation(
        key: Any?,
        enter: ViewAnimationDefinition,
    ): ViewSwitchAdapterKeyBuilder {
        definitions[key] = ViewSwitchDefinition(enter, enter.reversed())
        return keyBuilder(key)
    }

    fun bindEnterAnimation(
        key: Any?,
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: TransformationBuilder.() -> Unit
    ): ViewSwitchAdapterKeyBuilder {
        val definition = viewAnimation(interpolator, buildFlow)
        return bindEnterAnimation(key, definition)
    }

    fun bindExitAnimation(
        key: Any?,
        exit: ViewAnimationDefinition,
    ): ViewSwitchAdapterKeyBuilder {
        definitions[key] = ViewSwitchDefinition(exit.reversed(), exit)
        return keyBuilder(key)
    }

    fun bindExitAnimation(
        key: Any?,
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: TransformationBuilder.() -> Unit
    ): ViewSwitchAdapterKeyBuilder {
        val definition = viewAnimation(interpolator, buildFlow)
        return bindExitAnimation(key, definition)
    }

    fun bindViewSwitch(
        key: Any?,
        definition: ViewSwitchDefinition
    ): ViewSwitchAdapterKeyBuilder {
        definitions[key] = definition
        return keyBuilder(key)
    }

    fun bindViewSwitch(
        key: Any?, viewSwitchBuildFlow: ViewSwitchDefinitionBuilder.() -> Unit
    ): ViewSwitchAdapterKeyBuilder {
        val definition = viewSwitch(viewSwitchBuildFlow)
        return bindViewSwitch(key, definition)
    }

}