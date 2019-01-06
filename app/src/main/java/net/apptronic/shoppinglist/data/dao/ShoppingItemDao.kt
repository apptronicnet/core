package net.apptronic.shoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amakdev.budget.core.id.ID
import net.apptronic.shoppinglist.data.constants.ShoppingItemStatus
import net.apptronic.shoppinglist.data.entities.ShoppingItem

@Dao
interface ShoppingItemDao {

    @Query(
        "SELECT * FROM shopping_item WHERE status_id IN (${ShoppingItemStatus.TO_BUY}, ${ShoppingItemStatus.BOUGHT}) AND list_id = :listId"
    )
    fun getItemsForList(listId: ID): List<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(item: ShoppingItem)

}