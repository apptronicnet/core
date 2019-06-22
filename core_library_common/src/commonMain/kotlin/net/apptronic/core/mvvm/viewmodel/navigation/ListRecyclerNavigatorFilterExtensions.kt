package net.apptronic.core.mvvm.viewmodel.navigation

import kotlin.math.min

/**
 * Create [ListRecyclerNavigatorFilter] which uses [mappingFactory] to create [RecyclerListIndexMapping]
 */
fun mappingFactoryFilter(mappingFactory: (List<*>) -> RecyclerListIndexMapping): ListRecyclerNavigatorFilter {
    return StaticMappingFilter(mappingFactory)
}

private class StaticMappingFilter(
        private val mappingFactory: (List<*>) -> RecyclerListIndexMapping
) : ListRecyclerNavigatorFilter {

    override fun filter(items: List<ListItem>, listDescription: Any?): RecyclerListIndexMapping {
        return mappingFactory.invoke(items)
    }

}

/**
 * This mapping does not modify list.
 */
fun defaultMapping(source: List<*>): RecyclerListIndexMapping {
    return DefaultMapping(source)
}

private class DefaultMapping(private val source: List<*>) : RecyclerListIndexMapping {

    override fun getSize(): Int {
        return source.size
    }

    override fun mapIndex(sourceIndex: Int): Int {
        return sourceIndex
    }
}

/**
 * This mapping trims visibility of list to max size.
 */
fun trimLengthMapping(source: List<*>, maxSize: Int): RecyclerListIndexMapping {
    return TrimLengthMapping(source, maxSize)
}

private class TrimLengthMapping(
        private val source: List<*>,
        private val maxSize: Int
) : RecyclerListIndexMapping {

    override fun getSize(): Int {
        return min(source.size, maxSize)
    }

    override fun mapIndex(sourceIndex: Int): Int {
        return sourceIndex
    }

}

/**
 * This filter checks static items on start and takes all items until it's visible. When first not static item appears
 * it shows whole list.
 */
fun takeWhileVisibleStaticsOnStartFilter(): ListRecyclerNavigatorFilter {
    return TakeWhileVisibleStaticsOnStartFilter()
}

private class TakeWhileVisibleStaticsOnStartFilter : ListRecyclerNavigatorFilter {

    override fun filter(items: List<ListItem>, listDescription: Any?): RecyclerListIndexMapping {
        val firstNotStatic = items.indexOfFirst {
            !it.isStatic
        }
        return if (firstNotStatic == 0) {
            defaultMapping(items)
        } else {
            val firstNotVisibleIndex = (0 until firstNotStatic).firstOrNull {
                !items[it].isVisible
            }
            if (firstNotVisibleIndex != null) {
                trimLengthMapping(items, firstNotVisibleIndex)
            } else {
                defaultMapping(items)
            }
        }
    }

}