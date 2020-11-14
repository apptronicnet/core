package net.apptronic.core.entity.reflection

class TypeToIdMirror<T, Id>(val items: List<T>, val idProvider: (T) -> Id) {
}