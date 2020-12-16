package net.apptronic.core.commons.dataprovider

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

/**
 * Property which contains data provided by [DataProvider]. Allows to request data reload.
 */
interface DataProviderProperty<T> : Property<T> {

    /**
     * Execute reload data with [DataProvider].
     *
     * Updated data will be published for all clients of [DataProvider] including this [DataProviderProperty].
     *
     * In case of any [Exception] it will be thrown here and not published to [errors] of this or any other client of [DataProvider]
     */
    suspend fun reload(): T

    /**
     * Send reload request to [DataProvider] asynchronously
     *
     * Updated data will be published for all clients of [DataProvider] including this [DataProviderProperty]
     *
     * In case of any [Exception] it will be published to [errors] of this but NOT to other clients of [DataProvider]
     */
    fun postReload()

    /**
     * Retrieve errors from [DataProvider] and [postReload] requests
     */
    val errors: Entity<Exception>

}