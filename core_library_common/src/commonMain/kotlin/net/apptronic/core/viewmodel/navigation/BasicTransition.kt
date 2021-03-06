package net.apptronic.core.viewmodel.navigation

sealed class BasicTransition {

    /**
     * Defines transition with fade in / fade out of view which is on front.
     */
    object Fade : BasicTransition()

    /**
     * Defines transition when exiting view fading out when entering view fading in.
     */
    object Crossfade : BasicTransition()

    /**
     * Defines transition with opening new screen overlapping current
     */
    object Forward : BasicTransition()

    /**
     * Defines transition with closing screen which is overlapping previous
     */
    object Backward : BasicTransition()

    /**
     * Defines transition which means linear moving to next page
     */
    object Next : BasicTransition()

    /**
     * Defines transition which means linear moving to previous page
     */
    object Previous : BasicTransition()

}