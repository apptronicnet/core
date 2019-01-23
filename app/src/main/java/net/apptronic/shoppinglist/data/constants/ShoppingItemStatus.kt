package net.apptronic.shoppinglist.data.constants

enum class ShoppingItemStatus(val id: Int) : IntEnum {

    ToBuy(1),
    Bought(2),
    Deleted(3);

    override fun id(): Int {
        return id
    }

    companion object {
        const val TO_BUY = 1
        const val BOUGHT = 2
        const val DELETED = 3
    }

}