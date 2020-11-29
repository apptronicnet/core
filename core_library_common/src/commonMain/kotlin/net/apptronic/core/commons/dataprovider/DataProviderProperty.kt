package net.apptronic.core.commons.dataprovider

import net.apptronic.core.entity.base.Property

/**
 * Property which contains data provided by [DataProvider]. Allows to request data reload.
 */
interface DataProviderProperty<T> : Property<T> {

    suspend fun reload()

    fun postReload(ignoreErrors: Boolean)

}