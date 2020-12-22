package net.apptronic.core.commons.dataprovider.cache

internal fun <K, T> MutableMap<K, T>.trimToSizeFromMin(
        sizeFunction: (T) -> Int, maxSize: Int, orderBy: (T) -> Long
) {
    do {
        val contentSize = values.sumBy { sizeFunction(it) }
        if (contentSize > maxSize) {
            entries.minByOrNull {
                orderBy(it.value)
            }?.let {
                remove(it.key)
            }
        }
    } while (contentSize > maxSize && this.isNotEmpty())
}
