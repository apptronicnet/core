package net.apptronic.shoppinglist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amakdev.budget.core.id.ID
import net.apptronic.shoppinglist.data.constants.ShoppingListStatus

@Entity(tableName = "shopping_list")
data class ShoppingList(

        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: ID,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "status_id")
        var status: ShoppingListStatus

)