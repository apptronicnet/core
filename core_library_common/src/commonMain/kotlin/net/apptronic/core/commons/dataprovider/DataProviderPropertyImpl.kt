package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.commons.BaseProperty

internal class DataProviderPropertyImpl<T>(
        override val context: Context,
        private val holder: DataProviderHolder<*, T>
) : BaseProperty<T>(context), DataProviderProperty<T> {

    init {
        holder.provideData(context).subscribe(subject)
    }

    override suspend fun reload() {
        holder.executeReloadRequest()
    }

    override fun postReload(ignoreErrors: Boolean) {
        context.contextCoroutineScope.launch {
            try {
                holder.executeReloadRequest()
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