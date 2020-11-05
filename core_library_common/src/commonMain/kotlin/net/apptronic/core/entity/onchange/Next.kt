package net.apptronic.core.entity.onchange

/**
 * This class represents some value which requires additional information about it's change
 */
data class Next<T, E>(
        /**
         * The value itself which is passed or changed
         */
        val value: T,
        /**
         * The change information if present ot null if this is first value or if change information is not passed
         */
        val change: E?
)