package net.apptronic.core.commons.dataprovider

import net.apptronic.core.entity.base.Property

/**
 * Data provider client. Provides instance of [Property] containing data and allows to request data reload.
 */
interface DataProviderClient<T> {

    val data: Property<T>

    suspend fun reload()

    fun postReload(ignoreErrors: Boolean)

}