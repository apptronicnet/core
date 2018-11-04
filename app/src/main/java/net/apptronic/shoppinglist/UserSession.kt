package net.apptronic.shoppinglist

import android.content.Context
import net.apptronic.shoppinglist.data.DataRepository

fun Context.userSession(): UserSession? = (applicationContext as ApplicationContext).userSession

/**
 * Represents basic user session object
 */
interface UserSession {

    fun dataRepository(): DataRepository

}