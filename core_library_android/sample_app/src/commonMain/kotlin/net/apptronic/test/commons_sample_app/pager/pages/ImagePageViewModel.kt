package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.toggle
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.pager.NextPageNumberDescriptor
import net.apptronic.test.commons_sample_app.resources.ColorVariant
import net.apptronic.test.commons_sample_app.resources.ImageVariant
import kotlin.random.Random

private val IMAGE_VARIANTS = ImageVariant.values().toList()
private val COLOR_VARIANTS = ColorVariant.values().toList()

class ImagePageViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    private val pageNumber = inject(NextPageNumberDescriptor)

    val number = value("Image $pageNumber")

    val imageRes = toggle(IMAGE_VARIANTS, IMAGE_VARIANTS.random())
    val colorRes = toggle(COLOR_VARIANTS, COLOR_VARIANTS.random())

    fun onImageClick() {
        if (Random.nextBoolean()) {
            imageRes.toggle()
        } else {
            colorRes.toggle()
        }
    }

}