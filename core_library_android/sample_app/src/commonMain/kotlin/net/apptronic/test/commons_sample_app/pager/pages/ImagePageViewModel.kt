package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.component.toggle
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.pager.NextPageNumberDescriptor
import net.apptronic.test.commons_sample_app.resources.ColorVariant
import net.apptronic.test.commons_sample_app.resources.ImageVariant
import kotlin.random.Random

private val IMAGE_VARIANTS = ImageVariant.values().toList()
private val COLOR_VARIANTS = ColorVariant.values().toList()

class ImagePageViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    private val pageNumber = inject(NextPageNumberDescriptor)

    val number = value("Image $pageNumber")

    val imageRes = value(IMAGE_VARIANTS[Random.nextInt(IMAGE_VARIANTS.size)])
    val colorRes = value(COLOR_VARIANTS[Random.nextInt(COLOR_VARIANTS.size)])

    private val imageResToggle = toggle(imageRes, *IMAGE_VARIANTS.toTypedArray())
    private val colorResToggle = toggle(colorRes, *COLOR_VARIANTS.toTypedArray())

    fun onImageClick() {
        if (Random.nextBoolean()) {
            imageResToggle.toggle()
        } else {
            colorResToggle.toggle()
        }
    }

}