package net.apptronic.core.ios.anim.transition

/**
 * Defines order of views in transition for cases when order matters.
 */
enum class ViewTransitionDirectionSpec {
    /**
     * Transition supports only [ViewTransitionDirection.EnteringOnFront]
     */
    EnteringOnFront,

    /**
     * Transition supports only [ViewTransitionDirection.ExitingOnFront]
     */
    ExitingOnFront,

    /**
     * Views does not overlaps during transition, [ViewTransitionDirection] no matters
     */
    Irrelevant,

    /**
     * Views overlaps during transition but overlapping allows both [ViewTransitionDirection]s
     */
    Bidirectional
}