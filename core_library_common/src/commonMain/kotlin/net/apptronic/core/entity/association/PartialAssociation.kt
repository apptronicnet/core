package net.apptronic.core.entity.association

import net.apptronic.core.entity.base.Value

/**
 * Association of two [Entity]es where result value represents ony part of source value.
 *
 * In this case changing associated value should change only associated part, leaving other parts unchanged.
 */
interface PartialAssociation<T, E> {

    fun direct(value: T): E

    fun reverse(target: T, value: E): T

}

private class LambdaPartialAssociation<T, E>(
    private val source: Value<T>,
    private val directConversion: (T) -> E,
    private val reverseConversion: T.(E) -> T,
) : Association<T, E> {

    override fun direct(value: T): E {
        return directConversion(value)
    }

    override fun reverse(value: E): T {
        return source.get().reverseConversion(value)
    }

}

private class PartialAssociationWrapper<T, E>(
    private val source: Value<T>,
    private val targetAssociation: PartialAssociation<T, E>
) : Association<T, E> {

    override fun direct(value: T): E {
        return targetAssociation.direct(value)
    }

    override fun reverse(value: E): T {
        return targetAssociation.reverse(source.get(), value)
    }

}

/**
 * Creates association which represents part of source value. Reverse conversion should take existing source value
 * and apply changed value to source.
 *
 * For example, source can be Coordinate(x, y), which have 2 partial associations: x and y.
 * Changing x will not change y and changing y will not change x. When x in updated - Coordinate is changed, but y
 * remains same, and when y is updated - Coordinate is changed, but x remains same.
 */
fun <T, E> Value<T>.associatePart(
    association: PartialAssociation<T, E>
): Value<E> {
    return associate(PartialAssociationWrapper(this, association))
}

/**
 * Creates association which represents part of source value. Reverse conversion should take existing source value
 * and apply changed value to source.
 *
 * For example, source can be Coordinate(x, y), which have 2 partial associations: x and y.
 * Changing x will not change y and changing y will not change x. When x in updated - Coordinate is changed, but y
 * remains same, and when y is updated - Coordinate is changed, but x remains same.
 */
fun <T, E> Value<T>.associatePart(
    direct: (T) -> E,
    reverse: T.(E) -> T
): Value<E> {
    return associate(LambdaPartialAssociation(this, direct, reverse))
}