package net.apptronic.core.commons.dataprovider

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.di.SharedScopeManager
import net.apptronic.core.context.di.SharedScopeOwner

internal class DataProviderSharedScopeManager(context: Context, val descriptor: DataProviderDescriptor<*, *>) :
    Component(context), SharedScopeManager {

    private val dataDispatcher = inject(descriptor.dispatcherDescriptor)

    override fun onSharedScopeInitialized(owner: SharedScopeOwner) {
        dataDispatcher.observeDropDataRequest(context).subscribe {
            owner.resetAllFallbacks()
        }
    }

}