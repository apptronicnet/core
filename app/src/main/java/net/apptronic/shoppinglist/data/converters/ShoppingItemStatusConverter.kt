package net.apptronic.shoppinglist.data.converters

import androidx.room.TypeConverter
import net.apptronic.shoppinglist.data.constants.ShoppingItemStatus
import net.apptronic.shoppinglist.data.constants.valueFromId

class ShoppingItemStatusConverter {

    @TypeConverter
    fun toShoppingListStatus(value: Int): ShoppingItemStatus {
        return valueFromId(ShoppingItemStatus::class.java, value, ShoppingItemStatus.ToBuy)
    }

    @TypeConverter
    fun fromShoppingListStatus(value: ShoppingItemStatus): Int {
        return value.id()
    }

}