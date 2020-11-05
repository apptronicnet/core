package net.apptronic.core.view.engine

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.terminate
import net.apptronic.core.view.CoreViewContext

@UnderDevelopment
class ContextView<View : Any>(
        val view: View,
        private val context: CoreViewContext
) {

    fun terminate() {
        context.terminate()
    }

}