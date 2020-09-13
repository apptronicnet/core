package net.apptronic.core.android.anim

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import net.apptronic.core.android.anim.animations.Animation_Empty

fun viewSwitch(
    enterDefinition: ViewAnimationDefinition, exitDefinition: ViewAnimationDefinition
): ViewSwitchDefinition {
    return ViewSwitchDefinition(enterDefinition, exitDefinition)
}

fun viewSwitch(
    viewSwitchBuildFlow: ViewSwitchDefinitionBuilder.() -> Unit
): ViewSwitchDefinition {
    val builder = ViewSwitchDefinitionBuilder()
    builder.viewSwitchBuildFlow()
    return builder.build()
}

class ViewSwitchDefinition(
    val enter: ViewAnimationDefinition,
    val exit: ViewAnimationDefinition
)

class ViewSwitchDefinitionBuilder internal constructor() {

    private var enterDefinition = Animation_Empty
    private var exitDefinition = Animation_Empty

    fun enter(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: TransformationBuilder.() -> Unit
    ) {
        enterDefinition = viewAnimation(interpolator, buildFlow)
    }

    fun exit(
        interpolator: Interpolator = LinearInterpolator(),
        buildFlow: TransformationBuilder.() -> Unit
    ) {
        exitDefinition = viewAnimation(interpolator, buildFlow)
    }

    internal fun build(): ViewSwitchDefinition {
        return ViewSwitchDefinition(enterDefinition, exitDefinition)
    }

}