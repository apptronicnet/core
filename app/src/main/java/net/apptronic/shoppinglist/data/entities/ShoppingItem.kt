package net.apptronic.shoppinglist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amakdev.budget.core.id.ID
import net.apptronic.shoppinglist.data.constants.ShoppingItemStatus

@Entity(tableName = "shopping_item")
data class ShoppingItem(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: ID,

    @ColumnInfo(name = "list_id")
    var listId: ID,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "status_id")
    var status: ShoppingItemStatus

)