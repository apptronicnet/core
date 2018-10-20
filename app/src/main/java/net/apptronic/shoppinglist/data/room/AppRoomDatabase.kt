package net.apptronic.shoppinglist.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.apptronic.shoppinglist.data.converters.IDConverter
import net.apptronic.shoppinglist.data.converters.ShoppingListStatusConverter
import net.apptronic.shoppinglist.data.dao.ShoppingListDao
import net.apptronic.shoppinglist.data.entities.ShoppingList

const val DATABASE_FILE = "data.db"

fun buildRoomDatabase(context: Context): AppRoomDatabase {
    return Room.databaseBuilder(
            context, AppRoomDatabase::class.java, DATABASE_FILE)
            .build()
}

@Database(
        entities = [
            ShoppingList::class
        ], version = 1)
@TypeConverters(
        IDConverter::class,
        ShoppingListStatusConverter::class
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

}