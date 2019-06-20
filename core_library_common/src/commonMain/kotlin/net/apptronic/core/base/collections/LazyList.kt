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