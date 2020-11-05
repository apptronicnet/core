package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.behavior.delay
import net.apptronic.core.entity.newChain
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelWithVisibility

class StaticFilteredItemViewModel(
    parent: Context,
    private val itemId: String,
    private val loadingDelay: Long
) : ViewModel(parent, EmptyViewModelContext), ViewModelWithVisibility {

    private val isLoaded = value(false)

    override fun isReadyToShow(): Entity<Boolean> {
        return isLoaded
    }

    val text = value<String>()

    init {
        newChain().delay(loadingDelay).subscribe {
            text.set("$componentId loaded in $loadingDelay ms")
            isLoaded.set(true)
        }
    }

}