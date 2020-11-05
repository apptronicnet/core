package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.toggle
import net.apptronic.test.commons_sample_app.resources.ColorVariant
import net.apptronic.test.commons_sample_app.resources.ImageVariant
import kotlin.random.Random

private val IMAGE_VARIANTS = ImageVariant.values().toList()
private val COLOR_VARIANTS = ColorVariant.values().toList()

class ListItemImageViewModel(index: Int, parent: Context) : ListItemBaseViewModel(index, parent) {

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