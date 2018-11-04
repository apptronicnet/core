package net.apptronic.shoppinglist.data.constants

const val ShoppingListStatus_ACTIVE = 1
const val ShoppingListStatus_ARCHIVED = 2
const val ShoppingListStatus_DELETED = 3

enum class ShoppingListStatus(val id: Int) : IntEnum {

    Active(ShoppingListStatus_ACTIVE),
    Archived(ShoppingListStatus_ARCHIVED),
    Deleted(ShoppingListStatus_DELETED);

    override fun id(): Int {
        return id
    }

}