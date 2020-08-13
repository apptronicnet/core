package net.apptronic.core.mvvm.viewmodel.navigation

/**
 * Defines notification with info about view change transition
 */
class TransitionInfo(
        /**
         * Defines is new view should be on front
         */
        val isNewOnFront: Boolean,
        /**
         * Optional specification for transition animation
         */
        val spec: Any?
)