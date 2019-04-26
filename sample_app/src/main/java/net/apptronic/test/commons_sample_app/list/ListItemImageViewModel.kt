package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.component.context.Context
import net.apptronic.test.commons_sample_app.R
import kotlin.random.Random

private val IMAGE_VARIANTS =
    listOf(R.drawable.ic_list_item_1, R.drawable.ic_list_item_2, R.drawable.ic_list_item_3)
private val COLOR_VARIANTS =
    listOf(R.color.imageListItem1, R.color.imageListItem2, R.color.imageListItem3)

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