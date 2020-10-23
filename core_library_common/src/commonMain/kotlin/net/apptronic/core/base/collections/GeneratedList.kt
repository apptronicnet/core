package net.apptronic.core.base.collections

fun <E> generatedList(
        minValue: Int = 0,
        maxValue: Int = 0,
        generator: (Int) -> E
): GeneratedList<E> {
    return GeneratedList(minValue, maxValue, generator)
}

/**
 * Collection of items which are completely dynamically generated. Allows to creates lists of huge size without
 * needing to provide all items at same time. Items will be generated dynamically based on integer value of index.
 */
class GeneratedList<E>(
        /**
         * Defines minimum allowed value for generation list item
         */
        val minValue: Int,
        /**
         * Defines maximum allowed  value for generation list item
         */
        val maxValue: Int,
        /**
         * Generator function which converts integer value to list item
         */
        val generator: (Int) -> E
) : AbstractList<E>() {

    /**
     * Defines index of item which matches zero value
     */
    val indexOfZeroValue: Int = -minValue

    /**
     * Get item base on value instead of index
     */
    fun getAtValue(value: Int): E {
        if (value < minValue || value > maxValue) {
            throw IndexOutOfBoundsException()
        }
        return generator(value)
    }

    override fun get(index: Int): E {
        val value = minValue + index
        return getAtValue(value)
    }

    override val size: Int
        get() = maxValue - minValue + 1

    override fun indexOf(element: E): Int {
        throw UnsupportedOperationException()
    }

    override fun lastIndexOf(element: E): Int {
        throw UnsupportedOperationException()
    }

}