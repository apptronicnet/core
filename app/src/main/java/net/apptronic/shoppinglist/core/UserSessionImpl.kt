package net.apptronic.shoppinglist.core

import android.content.Context
import net.apptronic.shoppinglist.UserSession
import net.apptronic.shoppinglist.data.DataRepository
import net.apptronic.shoppinglist.data.impl.SQLiteDataRepository

class UserSessionImpl(val context: Context) : UserSession {

    private val dataRepository: DataRepository by lazy {
        SQLiteDataRepository(context)
    }

    override fun dataRepository(): DataRepository {
        return dataRepository
    }

}