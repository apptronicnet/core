package net.apptronic.core.component.context.local

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.LifecycleDefinition

class LocalContext internal constructor(
        private val parent: Context,
        override val defaultDispatcher: CoroutineDispatcher,
        lifecycleDefinition: LifecycleDefinition
) : SubContext(parent, lifecycleDefinition) {

    override fun getParent(): Context? = parent

}