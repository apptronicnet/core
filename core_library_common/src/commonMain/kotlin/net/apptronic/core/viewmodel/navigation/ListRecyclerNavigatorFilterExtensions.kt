package net.apptronic.core.viewmodel.navigation

import kotlin.math.min

/**
 * Create [ListRecyclerNavigatorFilter] which uses [mappingFactory] to create [DynamicListIndexMapping]
 */
fun mappingFactoryFilter(mappingFactory: (List<*>) -> DynamicListIndexMapping): ListRecyclerNavigatorFilter {
    return StaticMappingFilter(mappingFactory)
}

private class StaticMappingFilter(
        private val mappingFactory: (List<*>) -> DynamicListIndexMapping
) : ListRecyclerNavigatorFilter {

    override fun filter(items: List<ListItem>, listDescription: Any?): DynamicListIndexMapping {
        return mappingFactory.invoke(items)
    }

}

/**
 * This mapping does not modify list.
 */
fun defaultMapping(source: List<*>): DynamicListIndexMapping {
    return DefaultMapping(source)
}

private class DefaultMapping(private val source: List<*>) : DynamicListIndexMapping {

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
fun trimLengthMapping(source: List<*>, maxSize: Int): DynamicListIndexMapping {
    return TrimLengthMapping(source, maxSize)
}

private class TrimLengthMapping(
        private val source: List<*>,
        private val maxSize: Int
) : DynamicListIndexMapping {

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

    override fun filter(items: List<ListItem>, listDescription: Any?): DynamicListIndexMapping {
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