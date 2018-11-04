package net.apptronic.shoppinglist.data.impl

import android.content.Context
import com.amakdev.budget.core.id.DeviceId
import com.amakdev.budget.core.id.ID
import net.apptronic.shoppinglist.data.DataRepository
import net.apptronic.shoppinglist.data.room.AppRoomDatabase
import net.apptronic.shoppinglist.data.room.buildRoomDatabase

class SQLiteDataRepository(context: Context) : DataRepository {

    private val idGenerator = ID.newGenerator(DeviceId.fromValues(0L, 0L))

    private val database: AppRoomDatabase by lazy {
        buildRoomDatabase(context)
    }

    override fun idGenerator(): ID.Generator {
        return idGenerator
    }

}