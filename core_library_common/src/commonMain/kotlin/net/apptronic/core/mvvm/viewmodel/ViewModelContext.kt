package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext

class ViewModelContext internal constructor(
        parent: Context, private val name: String
) : SubContext(parent, VIEW_MODEL_LIFECYCLE) {

    override fun toString(): String {
        return "ViewModelContext/$name"
    }

}