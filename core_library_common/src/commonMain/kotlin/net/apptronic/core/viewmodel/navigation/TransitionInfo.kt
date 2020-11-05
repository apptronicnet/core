package net.apptronic.core.viewmodel.navigation

/**
 * Defines notification with info about view change transition.
 */
class TransitionInfo(
        /**
         * Defines is new view should be on front when on transition
         */
        val isNewOnFront: Boolean,
        /**
         * Optional specification for transition animation
         */
        val spec: Any?
)

internal fun Any?.castAsTransitionInfo(isNewOnFrontFallback: Boolean): TransitionInfo {
    return this as? TransitionInfo ?: TransitionInfo(isNewOnFrontFallback, this)
}