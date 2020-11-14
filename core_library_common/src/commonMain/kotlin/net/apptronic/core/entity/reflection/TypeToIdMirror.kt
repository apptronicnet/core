package net.apptronic.core.entity.reflection

class TypeToIdMirror<T, Id>(val items: List<T>, val idProvider: (T) -> Id) : Mirror<T?, Id?> {

    private val T?.id: Id?
        get() = if (this != null) idProvider(this) else null

    private val Id?.item: T?
        get() = items.firstOrNull {
            idProvider(it) == this
        }

    override fun direct(value: T?): Id? {
        return if (value != null) idProvider(value) else null
    }

    override fun reverse(value: Id?): T? {
        return items.firstOrNull {
            idProvider(it) == this
        }
    }

}