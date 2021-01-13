package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.commons.BaseProperty
import net.apptronic.core.entity.commons.typedEvent

internal class DataProviderPropertyImpl<T : Any>(
    override val context: Context,
    private val holder: DataProviderHolder<*, T>
) : BaseProperty<T>(context), DataProviderProperty<T> {

    override val errors = context.typedEvent<Exception>()

    init {
        holder.provideData(context).subscribe(subject)
        holder.observeErrors(context).subscribe(errors)
    }

    override suspend fun reload(): T {
        return holder.executeReloadRequest()
    }

    override fun postReload() {
        context.contextCoroutineScope.launch {
            try {
                holder.executeReloadRequest()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errors.update(e)
            }
        }
    }

}