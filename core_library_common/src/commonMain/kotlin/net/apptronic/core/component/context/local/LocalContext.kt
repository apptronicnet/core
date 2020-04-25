package net.apptronic.core.component.context.local

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle

class LocalContext internal constructor(
        private val parent: Context,
        override val defaultDispatcher: CoroutineDispatcher,
        private val lifecycle: LocalLifecycle
) : SubContext(parent) {

    override fun getParent(): Context? = parent

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}