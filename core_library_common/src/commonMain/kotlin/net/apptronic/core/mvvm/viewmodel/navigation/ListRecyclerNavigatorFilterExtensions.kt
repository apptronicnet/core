package net.apptronic.core.mvvm.viewmodel.navigation

fun mappingFactoryFilter(mappingFactory: (List<*>) -> RecyclerListIndexMapping): ListRecyclerNavigatorFilter {
    return StaticMappingFilter(mappingFactory)
}

private class StaticMappingFilter(
        private val mappingFactory: (List<*>) -> RecyclerListIndexMapping
) : ListRecyclerNavigatorFilter {

    override fun filter(items: List<ListItem>): RecyclerListIndexMapping {
        return mappingFactory.invoke(items)
    }

}

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

fun trimLengthMapping(source: List<*>, maxSize: Int): RecyclerListIndexMapping {
    return TrimLengthMapping(source, maxSize)
}

private class TrimLengthMapping(
        private val source: List<*>,
        private val maxSize: Int
) : RecyclerListIndexMapping {

    override fun getSize(): Int {
        val sourceSize = source.size
        return if (sourceSize < maxSize) maxSize else sourceSize
    }

    override fun mapIndex(sourceIndex: Int): Int {
        return sourceIndex
    }

}

fun takeVisibleStaticsOnStartFilter(): ListRecyclerNavigatorFilter {
    return TakeVisibleStaticsOnStartFilter()
}

private class TakeVisibleStaticsOnStartFilter : ListRecyclerNavigatorFilter {

    override fun filter(items: List<ListItem>): RecyclerListIndexMapping {
        val firstNotVisibleOrNotStatic = items.indexOfFirst {
            !it.isStatic || it.isVisible
        }
        return if (firstNotVisibleOrNotStatic >= 0) {
            TrimLengthMapping(items, firstNotVisibleOrNotStatic)
        } else {
            defaultMapping(items)
        }
    }

}