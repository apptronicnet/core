package net.apptronic.shoppinglist.data.converters

import androidx.room.TypeConverter
import com.amakdev.budget.core.id.ID

class IDConverter {

    @TypeConverter
    fun toID(value: String): ID {
        return ID.fromString(value)
    }

    @TypeConverter
    fun fromID(id: ID): String {
        return id.toString()
    }

}