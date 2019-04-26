package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import kotlin.random.Random

private val TEXT_VARIANTS =
    listOf("Hello!", "Let's go!", "Got it", "Always", "Never", "Some", "Another")

class ListItemTextViewModel(index: Int, parent: Context) : ListItemBaseViewModel(index, parent) {

    val text = value(TEXT_VARIANTS[Random.nextInt(TEXT_VARIANTS.size)])

    private val textToggle = toggle(text, *TEXT_VARIANTS.toTypedArray())

    fun onBodyClick() {
        textToggle.toggle()
    }

    override fun getName(): String {
        return "txt${index.get()}"
    }
}