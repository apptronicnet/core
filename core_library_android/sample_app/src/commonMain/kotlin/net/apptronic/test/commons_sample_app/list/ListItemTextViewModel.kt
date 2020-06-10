package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.toggle

private val TEXT_VARIANTS =
    listOf("Hello!", "Let's go!", "Got it", "Always", "Never", "Some", "Another")

class ListItemTextViewModel(index: Int, parent: Context) : ListItemBaseViewModel(index, parent) {

    val text = toggle(TEXT_VARIANTS, TEXT_VARIANTS.random())

    fun onBodyClick() {
        text.toggle()
    }

    override fun getName(): String {
        return "txt${index.get()}"
    }

}