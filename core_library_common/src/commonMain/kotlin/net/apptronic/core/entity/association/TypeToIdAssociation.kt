package net.apptronic.core.entity.association

class TypeToIdAssociation<T, Id>(val items: List<T>, val idProvider: (T) -> Id) : Association<T?, Id?> {

    override fun direct(value: T?): Id? {
        return if (value != null) idProvider(value) else null
    }

    override fun reverse(value: Id?): T? {
        return items.firstOrNull {
            idProvider(it) == value
        }
    }

}