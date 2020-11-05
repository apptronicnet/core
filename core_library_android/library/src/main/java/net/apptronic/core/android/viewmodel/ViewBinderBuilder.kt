package net.apptronic.core.android.viewmodel

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.viewmodel.IViewModel

@UnderDevelopment
private val ViewBinderBuilderExtension = extensionDescriptor<() -> ViewBinder<*>>()

@UnderDevelopment
fun <T : IViewModel> T.registerViewBinder(builder: () -> ViewBinder<T>) {
    extensions.set(ViewBinderBuilderExtension, builder)
}

@UnderDevelopment
fun <T : IViewModel> T.getViewBinder(): ViewBinder<T>? {
    return extensions[ViewBinderBuilderExtension]?.invoke() as? ViewBinder<T>
}