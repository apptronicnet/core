package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext

open class ViewModelContext : SubContext {

    private val name: String

    private val lifecycle: ViewModelLifecycle

    constructor(parent: Context) : super(parent, ViewModelLifecycle()) {
        this.lifecycle = ViewModelLifecycle()
        this.name = "ViewModelContext"
    }

    constructor(parent: Context, name: String) : super(parent, ViewModelLifecycle()) {
        this.lifecycle = ViewModelLifecycle()
        this.name = name
    }

    override fun getLifecycle(): ViewModelLifecycle {
        return lifecycle
    }

    override fun toString(): String {
        return super.toString() + "/" + name
    }

}