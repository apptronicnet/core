package net.apptronic.core.entity.reflection

import net.apptronic.core.entity.base.Value

private class PartialReflectionMirror<T, E>(
    private val source: Value<T>,
    private val directConversion: (T) -> E,
    private val reverseConversion: T.(E) -> T,
) : Mirror<T, E> {

    override fun direct(value: T): E {
        return directConversion(value)
    }

    override fun reverse(value: E): T {
        return source.get().reverseConversion(value)
    }

}

/**
 * Creates reflection which reflects part of source value. Reverse conversion should take existing source value
 * and apply changed value to source.
 *
 * For example, source can be Coordinate(x, y), which have 2 partial reflections: x and y.
 * Changing x will not change y and changing y will not change x. When x in updated - Coordinate is changed, but y
 * remains same, and when y is updated - Coordinate is changed, but x remains same.
 */
fun <T, E> Value<T>.reflectPart(
    direct: (T) -> E,
    reverse: T.(E) -> T
): Value<E> {
    return reflect(PartialReflectionMirror(this, direct, reverse))
}