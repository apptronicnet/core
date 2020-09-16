package net.apptronic.core.android.viewmodel.transition2

import android.view.View
import net.apptronic.core.android.anim.ViewAnimationSet

fun viewSwitchAdapter(buildFlow: ViewSwitchAdapterBuilder.() -> Unit): ViewSwitchAdapter {
    val builder = ViewSwitchAdapterBuilder()
    builder.buildFlow()
    return BuilderViewSwitchAdapter(builder)
}

class BuilderViewSwitchAdapter internal constructor(
    private val builder: ViewSwitchAdapterBuilder
) : ViewSwitchAdapter {

    override fun buildViewSwitch(
        enter: View?,
        exit: View?,
        container: View,
        duration: Long,
        transitionSpec: Any?
    ): ViewAnimationSet? {
        val definition = builder.definitions[transitionSpec]
        return if (definition != null) {
            val targetDuration = builder.durations[transitionSpec] ?: duration
            val multipliedDuration = builder.durationMultipliers[transitionSpec]?.let {
                (targetDuration * it).toLong()
            } ?: targetDuration
            definition.buildAnimationSet(enter, exit, container, multipliedDuration)
        } else null
    }

}