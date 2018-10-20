package net.apptronic.shoppinglist

import android.app.Application
import net.apptronic.shoppinglist.core.UserSessionImpl

class ApplicationContext : Application() {

    var userSession: UserSession? = null

    override fun onCreate() {
        super.onCreate()
        userSession = UserSessionImpl(this)
    }

}