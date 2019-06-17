package net.apptronic.core.base.collections

/**
 * Creates list which holds [sources] and unwraps items from it dynamically by request. Allows to combine very large
 * or lazy lists without immediate getting all items from all [sources]
 */
fun <E> wrapLists(sources: List<List<E>>): List<E> {
    return UnwrappedListOfList(sources)
}

/**
 * Creates list which holds [sources] and unwraps items from it dynamically by request. Allows to combine very large
 * or lazy lists without immediate getting all items from all [sources]
 */
fun <E> wrapLists(vararg sources: List<E>): List<E> {
    return UnwrappedListOfList(sources.toList())
}

/**
 * List which holds [sources] and unwraps items from it dynamically by request. Allows to combine very large
 * or lazy lists without immediate getting all items from all [sources]
 */
class UnwrappedListOfList<E>(
        private val sources: List<List<E>>
) : AbstractList<E>() {

    override val size: Int
        get() {
            return sources.sumBy { it.size }
        }

    override fun get(index: Int): E {
        if (index < 0) {
            throw IndexOutOfBoundsException()
        }
        var indexOfList = -1
        var startOfList = 0
        var untilOfList = 0
        do {
            indexOfList++
            if (indexOfList >= sources.size) {
                throw IndexOutOfBoundsException()
            }
            startOfList = untilOfList
            untilOfList = startOfList + sources[indexOfList].size
        } while (index >= untilOfList)
        return sources[indexOfList][index - startOfList]
    }

}