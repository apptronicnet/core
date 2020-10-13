package net.apptronic.core.base.collections

fun <T, E> lazyListOf(source: List<T>, mapFunction: (List<T>, Int) -> E): List<E> {
    return LazyList(source, mapFunction, { it.size })
}

fun <T, E> simpleLazyListOf(source: List<T>, mapFunction: (T) -> E): List<E> {
    return LazyList(source, { sourceList, index ->
        mapFunction.invoke(sourceList[index])
    }, { it.size })
}

fun <T, E> lazyListOf(source: T, size: Int, mapFunction: (T, Int) -> E): List<E> {
    return LazyList(source, mapFunction, { size })
}

fun <T, E> lazyListOf(source: T, sizeFunction: (T) -> Int, mapFunction: (T, Int) -> E): List<E> {
    return LazyList(source, mapFunction, sizeFunction)
}

fun <T, E> dynamicLazyListOf(source: List<T>, mapFunction: (T) -> E): List<E> {
    return DynamicLazyList(source, mapFunction)
}

private class LazyList<T, E>(
        private val source: T,
        private val mapFunction: (T, Int) -> E,
        private val sizeFunction: (T) -> Int
) : AbstractList<E>() {

    private val converted = mutableMapOf<Int, E>()

    override val size: Int
        get() = sizeFunction(source)

    override fun get(index: Int): E {
        return converted[index] ?: run {
            val result = mapFunction.invoke(source, index)
            converted[index] = result
            result
        }
    }

}

private class DynamicLazyList<T, E>(
        private val source: List<T>,
        private val mapFunction: (T) -> E,
) : AbstractList<E>() {

    override val size: Int
        get() = source.size

    override fun get(index: Int): E {
        return mapFunction(source[index])
    }

}