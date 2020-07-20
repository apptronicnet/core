package net.apptronic.core.android.viewmodel.transitions.transformation

class TransformationDescriptor internal constructor(val name: String)

fun transformationDescriptor(name: String) = TransformationDescriptor(name)