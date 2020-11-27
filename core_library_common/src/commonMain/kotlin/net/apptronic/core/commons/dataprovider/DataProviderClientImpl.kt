package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.base.Property

internal class DataProviderClientImpl<T>(
        context: Context,
        private val holder: DataProviderHolder<T, *>
) : Component(context), DataProviderClient<T> {

    override val data: Property<T> = holder.provideData(context)

    override suspend fun reload() {
        holder.executeReloadRequest()
    }

    override fun postReload(ignoreErrors: Boolean) {
        contextCoroutineScope.launch {
            try {

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (ignoreErrors) {
                    e.printStackTrace()
                } else {
                    throw e
                }
            }
        }
    }

}