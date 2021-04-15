package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.toggle

private val TEXT_VARIANTS =
    listOf("Hello!", "Let's go!", "Got it", "Always", "Never", "Some", "Another")

fun Contextual.listItemTextViewModel(index: Int) =
    ListItemTextViewModel(childContext(), index)

class ListItemTextViewModel internal constructor(context: Context, index: Int) :
    ListItemBaseViewModel(context, index) {

    val text = toggle(TEXT_VARIANTS, TEXT_VARIANTS.random())

    fun onBodyClick() {
        text.toggle()
    }

    override fun getName(): String {
        return "txt${index.get()}"
    }

}