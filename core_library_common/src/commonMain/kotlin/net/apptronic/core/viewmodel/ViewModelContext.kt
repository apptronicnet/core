package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.SubContext

class ViewModelContext internal constructor(
        parent: Context, private val name: String
) : SubContext(parent, VIEW_MODEL_LIFECYCLE) {

    override val context: ViewModelContext
        get() = this

    override fun toString(): String {
        return "ViewModelContext/$name"
    }

}