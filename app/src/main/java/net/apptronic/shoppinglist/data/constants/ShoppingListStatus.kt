package net.apptronic.shoppinglist.data.constants

enum class ShoppingListStatus(val id: Int) : IntEnum {

    Active(1),
    Archived(2),
    Deleted(3);

    override fun id(): Int {
        return id
    }

    companion object {
        const val ACTIVE = 1
        const val ARCHIVED = 2
        const val DELETED = 3
    }

}