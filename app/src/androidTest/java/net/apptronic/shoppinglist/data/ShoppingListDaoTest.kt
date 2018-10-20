package net.apptronic.shoppinglist.data

import android.support.test.runner.AndroidJUnit4
import net.apptronic.shoppinglist.data.constants.ShoppingListStatus
import net.apptronic.shoppinglist.data.entities.ShoppingList
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListDaoTest : BaseDatabaseTest() {

    @Test
    fun trySaveReadData() {
        val shoppingList = ShoppingList(
                id = generateId(),
                name = "Test name",
                status = ShoppingListStatus.Active
        )
        database().shoppingListDao().save(shoppingList)
        val result = database().shoppingListDao().getActiveLists()
        assert(result.size == 1)
        assert(shoppingList == result[0])
    }

}