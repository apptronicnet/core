package net.apptronic.shoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.apptronic.shoppinglist.data.constants.ShoppingListStatus_ACTIVE
import net.apptronic.shoppinglist.data.entities.ShoppingList

@Dao
interface ShoppingListDao {

    @Query("SELECT * from shopping_list WHERE status_id = $ShoppingListStatus_ACTIVE")
    fun getActiveLists(): List<ShoppingList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: ShoppingList)

}