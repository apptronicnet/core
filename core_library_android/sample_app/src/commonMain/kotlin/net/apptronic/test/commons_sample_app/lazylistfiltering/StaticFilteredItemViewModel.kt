package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.behavior.delay
import net.apptronic.core.entity.commons.unitEntity
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelWithVisibility

fun Contextual.staticFilteredItemViewModel(itemId: String, loadingDelay: Long) =
    StaticFilteredItemViewModel(childContext(), itemId, loadingDelay)

class StaticFilteredItemViewModel(
    context: Context, private val itemId: String, private val loadingDelay: Long
) : ViewModel(context), ViewModelWithVisibility {

    private val isLoaded = value(false)

    override fun isReadyToShow(): Entity<Boolean> {
        return isLoaded
    }

    val text = value<String>()

    init {
        unitEntity().delay(loadingDelay).subscribe {
            text.set("$componentId loaded in $loadingDelay ms")
            isLoaded.set(true)
        }
    }

}