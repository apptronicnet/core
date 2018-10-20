package net.apptronic.shoppinglist.data

import com.amakdev.budget.core.id.ID

interface DataRepository {

    fun idGenerator(): ID.Generator

}