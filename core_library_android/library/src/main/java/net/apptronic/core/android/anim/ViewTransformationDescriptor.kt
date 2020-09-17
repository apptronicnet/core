package net.apptronic.core.android.anim

class ViewTransformationDescriptor internal constructor(val name: String)

fun viewTransformationDescriptor(name: String) = ViewTransformationDescriptor(name)