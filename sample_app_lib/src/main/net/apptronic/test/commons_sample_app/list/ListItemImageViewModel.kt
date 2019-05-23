package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.test.commons_sample_app.resources.ColorVariant
import net.apptronic.test.commons_sample_app.resources.ImageVariant
import kotlin.random.Random

private val IMAGE_VARIANTS = ImageVariant.values().toList()
private val COLOR_VARIANTS = ColorVariant.values().toList()

class ListItemImageViewModel(index: Int, parent: Context) : ListItemBaseViewModel(index, parent) {

    val imageRes = value(IMAGE_VARIANTS[Random.nextInt(IMAGE_VARIANTS.size)])
    val colorRes = value(COLOR_VARIANTS[Random.nextInt(COLOR_VARIANTS.size)])

    private val imageResToggle = toggle(imageRes, *IMAGE_VARIANTS.toTypedArray())
    private val colorResToggle = toggle(colorRes, *COLOR_VARIANTS.toTypedArray())

    fun onBodyClick() {
        if (Random.nextBoolean()) {
            imageResToggle.toggle()
        } else {
            colorResToggle.toggle()
        }
    }

    override fun getName(): String {
        return "img${index.get()}"
    }

}