package net.apptronic.shoppinglist.data

import android.support.test.InstrumentationRegistry
import androidx.room.Room
import com.amakdev.budget.core.id.DeviceId
import com.amakdev.budget.core.id.ID
import net.apptronic.shoppinglist.data.room.AppRoomDatabase
import org.junit.After
import org.junit.Before

open class BaseDatabaseTest {

    companion object {
        private val idGenerator = ID.newGenerator(DeviceId.fromValues(0L, 0L))
    }

    private lateinit var database: AppRoomDatabase

    @Before
    fun before() {
        try {
            database = Room.inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getTargetContext(),
                    AppRoomDatabase::class.java)
                    .build()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        doBefore()
    }

    open fun doBefore() {
        // implement by subclass if needed
    }

    @After
    fun after() {
        doAfter()
        database.close()
    }

    open fun doAfter() {
        // implement by subclass if needed
    }

    fun database(): AppRoomDatabase {
        return database
    }

    fun generateId(): ID {
        return idGenerator.generateId()
    }

}