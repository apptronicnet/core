package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.component.toggle
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
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