package net.apptronic.core.base.collections

fun <T, E> lazyListOf(source: List<T>, mapFunction: (List<T>, Int) -> E): List<E> {
    return LazyList(source, mapFunction)
}

fun <T, E> simpleLazyListOf(source: List<T>, mapFunction: (T) -> E): List<E> {
    return LazyList(source) { sourceList, index ->
        mapFunction.invoke(sourceList[index])
    }
}

private class LazyList<T, E>(
        private val source: List<T>,
        private val mapFunction: (List<T>, Int) -> E
) : AbstractList<E>() {

    private val converted = mutableMapOf<Int, E>()

    override val size: Int
        get() = source.size

    override fun get(index: Int): E {
        return converted[index] ?: run {
            val result = mapFunction.invoke(source, index)
            converted[index] = result
            result
        }
    }

}