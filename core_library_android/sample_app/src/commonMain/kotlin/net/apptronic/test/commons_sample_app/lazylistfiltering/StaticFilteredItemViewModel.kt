package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.delay
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.newChain
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelWithVisibility

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
            text.set("$id loaded in $loadingDelay ms")
            isLoaded.set(true)
        }
    }

}