package net.apptronic.shoppinglist.data.converters

import androidx.room.TypeConverter
import net.apptronic.shoppinglist.data.constants.ShoppingListStatus
import net.apptronic.shoppinglist.data.constants.valueFromId

class ShoppingListStatusConverter {

    @TypeConverter
    fun toShoppingListStatus(value: Int): ShoppingListStatus {
        return valueFromId(ShoppingListStatus::class.java, value, ShoppingListStatus.Active)
    }

    @TypeConverter
    fun fromShoppingListStatus(value: ShoppingListStatus): Int {
        return value.id()
    }

}