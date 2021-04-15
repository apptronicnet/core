package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.toggle
import net.apptronic.test.commons_sample_app.resources.ColorVariant
import net.apptronic.test.commons_sample_app.resources.ImageVariant
import kotlin.random.Random

private val IMAGE_VARIANTS = ImageVariant.values().toList()
private val COLOR_VARIANTS = ColorVariant.values().toList()

fun Contextual.listItemImageViewModel(index: Int) =
    ListItemImageViewModel(childContext(), index)

class ListItemImageViewModel(context: Context, index: Int) : ListItemBaseViewModel(context, index) {

    val imageRes = toggle(IMAGE_VARIANTS, IMAGE_VARIANTS.random())
    val colorRes = toggle(COLOR_VARIANTS, COLOR_VARIANTS.random())

    fun onBodyClick() {
        if (Random.nextBoolean()) {
            imageRes.toggle()
        } else {
            colorRes.toggle()
        }
    }

    override fun getName(): String {
        return "img${index.get()}"
    }

}